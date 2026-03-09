package org.example.projectmanagementsystem.service.temp;

import org.example.projectmanagementsystem.dto.request.ProjectCreateDto;
import org.example.projectmanagementsystem.dto.request.ProjectUpdateDto;
import org.example.projectmanagementsystem.dto.response.AssignPmDto;
import org.example.projectmanagementsystem.dto.response.ProjectResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;

public interface ProjectService {

    ProjectResponseDto createProject(ProjectCreateDto dto) throws AccessDeniedException;

    ProjectResponseDto updateProject(Long id, ProjectUpdateDto dto) throws AccessDeniedException;

    void deleteProject(Long id) throws AccessDeniedException;

    Page<ProjectResponseDto> getAllProjects(Pageable pageable) throws AccessDeniedException;

    ProjectResponseDto getProjectById(Long id) throws AccessDeniedException;

    ProjectResponseDto assignPm(Long projectId, AssignPmDto dto);

}
