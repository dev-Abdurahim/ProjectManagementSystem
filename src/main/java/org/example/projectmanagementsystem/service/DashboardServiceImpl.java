package org.example.projectmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagementsystem.dto.dashboard.*;
import org.example.projectmanagementsystem.entity.KpiGoal;
import org.example.projectmanagementsystem.projection.MonthlyRevenueProjection;
import org.example.projectmanagementsystem.projection.TypeRevenueProjection;
import org.example.projectmanagementsystem.repository.KpiGoalRepository;
import org.example.projectmanagementsystem.repository.MonthlyAllocationRepository;
import org.example.projectmanagementsystem.repository.ProjectRepository;
import org.example.projectmanagementsystem.service.temp.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProjectRepository projectRepository;
    private final MonthlyAllocationRepository monthlyAllocationRepository;
    private final KpiGoalRepository kpiGoalRepository;


    @Override
    public DashboardOverviewDto getOverview(int year) {
        return DashboardOverviewDto.builder()
                .year(year)
                .summary(buildSummary(year))
                .monthlyRevenue(buildMonthlyRevenue(year))
                .typeShares(buildTypeShares(year))
                .topProjects(buildTopProjects(year))
                .delayedPms(buildDelayedPms())
                .build();
    }

    //  berilgan yil uchun dashboard statistikalarini hisoblaydi kpini yani foizni ✅
    private DashboardSummaryResponse buildSummary(int year){
        BigDecimal actualRevenue = monthlyAllocationRepository.findActualRevenueByYear(year);
        actualRevenue = actualRevenue != null ? actualRevenue : BigDecimal.ZERO;

        BigDecimal targetRevenue = kpiGoalRepository.findByYear(year)
                .map(KpiGoal::getTargetRevenue)
                .orElse(BigDecimal.ZERO);

        BigDecimal difference = actualRevenue.subtract(targetRevenue);

        BigDecimal achievementRate = targetRevenue.compareTo(BigDecimal.ZERO) > 0
                ? actualRevenue
                .divide(targetRevenue, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP)
                :BigDecimal.ZERO;
        return DashboardSummaryResponse.builder()
                .totalProjects(projectRepository.countTotal())
                .delayedProjects(projectRepository.countDelayed())
                .ongoingProjects(projectRepository.countOngoing())
                .droppedProjects(projectRepository.countDropped())
                .actualRevenue(actualRevenue)
                .targetRevenue(targetRevenue)
                .difference(difference)
                .achievementRate(achievementRate)
                .build();
    }

    // line chart uchun ✅
    private List<MonthlyAllocationDto> buildMonthlyRevenue(int year){
        Map<Integer, BigDecimal> monthMap = monthlyAllocationRepository.findMonthlyRevenueByYear(year)
                .stream()
                .collect(Collectors.toMap(
                        MonthlyRevenueProjection::getMonth,
                        MonthlyRevenueProjection::getTotal
                ));
        return IntStream.rangeClosed(1,12)
                .mapToObj(month -> MonthlyAllocationDto.builder()
                        .monthYear(YearMonth.of(year,month).toString())
                        .amount(monthMap.getOrDefault(month,BigDecimal.ZERO))
                        .build())
                .collect(Collectors.toList());
    }

    // har bir project type ning umumiy revenue ichidagi foiz ulushini hisoblaydi ✅
    private List<TypeRevenueShare> buildTypeShares(int year) {
        List<TypeRevenueProjection> projections = projectRepository.findRevenueByType(year);

        // buyoda har bir project type boyicha miqdorini bir biriga qoshadi
        BigDecimal grandTotal = projections.stream()
                .map(TypeRevenueProjection::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return projections.stream()
                .map(p -> {
                    BigDecimal percentage = grandTotal.compareTo(BigDecimal.ZERO) > 0
                            ? p.getTotal()
                            .divide(grandTotal, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(1, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;
                    return TypeRevenueShare.builder()
                            .type(p.getType())
                            .total(p.getTotal())
                            .percentage(percentage)
                            .build();
                })
                .collect(Collectors.toList());

    }

    private List<TopProjectItem> buildTopProjects(int year){
        return projectRepository.findTop4ProjectsByRevenue(year)
                .stream()
                .map(p -> TopProjectItem.builder()
                        .projectName(p.getProjectName())
                        .revenue(p.getRevenue())
                        .build())
                .collect(Collectors.toList());
    }
    private List<PmDelayItem> buildDelayedPms(){
        return projectRepository.findDelayedPms()
                .stream()
                .map(p -> PmDelayItem.builder()
                        .pmName(p.getPmName())
                        .delayedCount(p.getDelayedCount())
                        .averageDelayPercent(p.getAverageDelayPercent()
                                .setScale(1,RoundingMode.HALF_UP))
                        .build())
                .collect(Collectors.toList());
    }
    @Transactional
    @Override
    public DashboardSummaryResponse setKpiGoal(int year, BigDecimal targetRevenue) {
        KpiGoal goal = kpiGoalRepository.findByYear(year)
                .orElse(new KpiGoal());
        goal.setYear(year);
        goal.setTargetRevenue(targetRevenue);
        kpiGoalRepository.save(goal);
        return buildSummary(year);
    }
}
