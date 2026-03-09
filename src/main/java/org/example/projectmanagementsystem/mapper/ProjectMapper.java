package org.example.projectmanagementsystem.mapper;

import org.example.projectmanagementsystem.dto.request.ProjectCreateDto;
import org.example.projectmanagementsystem.dto.request.ProjectUpdateDto;
import org.example.projectmanagementsystem.dto.response.ProjectResponseDto;
import org.example.projectmanagementsystem.entity.Project;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = MonthlyRevenueMapper.class)
public interface ProjectMapper {

      @Mapping(target = "workingDays", ignore = true)
      @Mapping(target = "monthlyAllocations", ignore = true)
      @Mapping(target = "deleted", constant = "false")
      @Mapping(target = "name", source = "projectName")
      @Mapping(target = "owner", ignore = true)
      @Mapping(target = "status",ignore = true)
      @Mapping(target = "projectManager", ignore = true)
      Project toEntity(ProjectCreateDto dto);

      @Mapping(target = "name", source = "projectName")
      @Mapping(target = "projectManager", ignore = true)
      @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
      void updateEntityFromDto(ProjectUpdateDto dto, @MappingTarget Project entity);

      @Mapping(target = "projectName", source = "name")
      @Mapping(target = "ownerUsername", source = "owner.username")
      ProjectResponseDto toResponseDto(Project entity);

    List<ProjectResponseDto> toResponseDtoList(List<Project> entities);

}
