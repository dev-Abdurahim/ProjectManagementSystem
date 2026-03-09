package org.example.projectmanagementsystem.service;


import lombok.RequiredArgsConstructor;
import org.example.projectmanagementsystem.dto.request.ProjectCreateDto;
import org.example.projectmanagementsystem.dto.request.ProjectUpdateDto;
import org.example.projectmanagementsystem.dto.response.AssignPmDto;
import org.example.projectmanagementsystem.dto.response.ProjectResponseDto;
import org.example.projectmanagementsystem.entity.MonthlyAllocation;
import org.example.projectmanagementsystem.entity.Project;
import org.example.projectmanagementsystem.entity.User;
import org.example.projectmanagementsystem.enums.ProjectStatus;
import org.example.projectmanagementsystem.enums.UserRole;
import org.example.projectmanagementsystem.exception.ApiException;
import org.example.projectmanagementsystem.exception.ErrorCode;
import org.example.projectmanagementsystem.exception.ProjectNotFoundException;
import org.example.projectmanagementsystem.mapper.ProjectMapper;
import org.example.projectmanagementsystem.repository.ProjectRepository;
import org.example.projectmanagementsystem.repository.UserRepository;
import org.example.projectmanagementsystem.service.temp.ProjectService;
import org.example.projectmanagementsystem.util.SecurityUtils;
import org.example.projectmanagementsystem.util.WorkingDaysCalculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectResponseDto createProject(ProjectCreateDto dto) throws AccessDeniedException {
        validateDates(dto.getStartDate(),dto.getEndDate());

        Project project = projectMapper.toEntity(dto);
        project.setOwner(SecurityUtils.getCurrentUser());
        project.setStatus(ProjectStatus.PLANNED);
        project.setProjectManager(null);
        project.setWorkingDays(WorkingDaysCalculator.calculate(dto.getStartDate(),dto.getEndDate()));

        project = projectRepository.save(project);
        createMonthlyAllocations(project, dto.getRevenue());
        return projectMapper.toResponseDto(project);
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProject(Long id, ProjectUpdateDto dto) throws AccessDeniedException {

        Project project = resolveProjectForWrite(id);

        LocalDate oldStartDate = project.getStartDate();
        LocalDate oldEndDate = project.getEndDate();
        BigDecimal oldRevenue = project.getRevenue();

        projectMapper.updateEntityFromDto(dto,project);

        validateDates(project.getStartDate(),project.getEndDate());

        boolean datesChanged =
                !Objects.equals(oldStartDate, project.getStartDate()) ||
                        !Objects.equals(oldEndDate, project.getEndDate());

        boolean revenueChanged =
                !Objects.equals(oldRevenue, project.getRevenue());

        if(datesChanged){
          project.setWorkingDays(WorkingDaysCalculator.calculate(project.getStartDate(),project.getEndDate()));
        }
        if(datesChanged || revenueChanged){
            project.getMonthlyAllocations().clear();
            createMonthlyAllocations(project, project.getRevenue());
        }

        return projectMapper.toResponseDto(project);

    }


    @Override
    @Transactional
    public void deleteProject(Long id) throws AccessDeniedException {
        Project project = resolveProjectForDelete(id);
        project.setDeleted(true);
    }

    @Override
    public Page<ProjectResponseDto> getAllProjects(Pageable pageable) throws AccessDeniedException {
        Page<Project> page;

        if(SecurityUtils.hasRole(UserRole.ADMIN)){
           page =  projectRepository.findAllProjectProjections(pageable);
        }
        else if (SecurityUtils.hasRole(UserRole.PM)) {
            page = projectRepository.findAllByProjectManager(SecurityUtils.getCurrentUser().getFullName(),pageable);

        }
        else {
            page = projectRepository.findAllByOwnerId(SecurityUtils.getCurrentUserId(),pageable);
        }
        return page.map(projectMapper::toResponseDto);
    }

    @Override
    public ProjectResponseDto getProjectById(Long id) throws AccessDeniedException {
        Project project = resolveProjectForRead(id);
        return projectMapper.toResponseDto(project);
    }

    @Override
    @Transactional
    public ProjectResponseDto assignPm(Long projectId, AssignPmDto dto) {
        Project project  = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        User pm = userRepository.findById(dto.getPmUserId())
                .orElseThrow(() -> new ApiException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + dto.getPmUserId(),
                        HttpStatus.NOT_FOUND
                ));
        boolean isPm = pm.getRoles().contains(UserRole.PM);
        if(!isPm){
            throw new ApiException(
                    ErrorCode.BUSINESS_RULE_VIOLATION,
                    "User with id " + dto.getPmUserId() + " does not have PM role",
                    HttpStatus.BAD_REQUEST
            );
        }
        project.setProjectManager(pm.getFullName());
        projectRepository.save(project);
        return projectMapper.toResponseDto(project);

    }

    private void createMonthlyAllocations(Project project,BigDecimal revenue) {

        YearMonth startMonth = YearMonth.from(project.getStartDate());
        YearMonth endMonth = YearMonth.from(project.getEndDate());

        //Bu ikki oy orasidagi farqni beradi.
        long months = startMonth.until(endMonth, ChronoUnit.MONTHS) + 1;

        if(months <= 0){
            return;
        }


        BigDecimal monthlyAmount = revenue.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);

        YearMonth currentMonth = startMonth;

        for (int i = 0; i < months; i++){

            // Bu — bitta oy uchun ajratilgan summa degani.
            MonthlyAllocation allocation = new MonthlyAllocation();
            allocation.setProject(project);
            allocation.setMonthYear(currentMonth.atDay(1));
            allocation.setAmount(monthlyAmount);

            project.getMonthlyAllocations().add(allocation);

            currentMonth = currentMonth.plusMonths(1);
        }
        
    }

    // ═══════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═══════════════════════════════════════════════


    private void validateDates(LocalDate startDate, LocalDate endDate){
        if(endDate.isBefore(startDate)){
            throw new ApiException(
                    ErrorCode.BUSINESS_RULE_VIOLATION,
                    "End date cannot be before start date",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * READ uchun: har bir role o'z scope'ida projectni topadi.
     */

    private Project resolveProjectForRead(Long id) throws AccessDeniedException {
        if(SecurityUtils.hasRole(UserRole.ADMIN)){
            return projectRepository.findById(id)
                    .orElseThrow(() -> new ProjectNotFoundException(id));
        }
        if(SecurityUtils.hasRole(UserRole.PM)){
            return projectRepository.findByIdAndProjectManager(id,SecurityUtils.getCurrentUser().getFullName())
                    .orElseThrow(() -> new ProjectNotFoundException(id));
        }
        //User
        return projectRepository.findByIdAndOwnerId(id,SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }

    /**
     * WRITE (update) uchun: ADMIN update qila olmaydi — faqat USER va PM.
     */

    private Project resolveProjectForWrite(Long id) throws AccessDeniedException {
        if(SecurityUtils.hasRole(UserRole.ADMIN)){
            throw new AccessDeniedException("Admin loyihani update qila olmaydi");
        }
        if(SecurityUtils.hasRole(UserRole.PM)){
            return projectRepository.findByIdAndProjectManager(id,SecurityUtils.getCurrentUser().getFullName())
                    .orElseThrow(() -> new ProjectNotFoundException(id));
        }
        return projectRepository.findByIdAndOwnerId(id,SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }

    private Project resolveProjectForDelete(Long id) throws AccessDeniedException {
        if(SecurityUtils.hasRole(UserRole.ADMIN)){
            return projectRepository.findById(id)
                    .orElseThrow(() -> new ProjectNotFoundException(id));
        }
        if(SecurityUtils.hasRole(UserRole.PM)){
            throw new AccessDeniedException("PM loyihani o'chira olmaydi");
        }
        return projectRepository.findByIdAndOwnerId(id,SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }


}
