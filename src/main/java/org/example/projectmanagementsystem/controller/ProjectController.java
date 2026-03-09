package org.example.projectmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagementsystem.dto.request.ProjectCreateDto;
import org.example.projectmanagementsystem.dto.request.ProjectUpdateDto;
import org.example.projectmanagementsystem.dto.response.AssignPmDto;
import org.example.projectmanagementsystem.dto.response.ProjectResponseDto;
import org.example.projectmanagementsystem.service.temp.ProjectService;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Tag(name = "Projects", description = "Loyihalarni boshqarish API'lari")
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Yangi loyiha yaratish", description = "Yangi loyiha qo'shadi va monthly allocations avto yaratadi")
    @ApiResponse(responseCode = "201", description = "Loyiha muvaffaqiyatli yaratildi")
    @PreAuthorize("hasAnyRole('ADMIN','USER','PM')")
    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectCreateDto dto) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(dto));
    }

    @Operation(summary = "Loyiha ma'lumotlarini yangilash", description = "Mavjud loyihani partial update qiladi")
    @ApiResponse(responseCode = "200", description = "Loyiha yangilandi")
    @ApiResponse(responseCode = "404", description = "Loyiha topilmadi")
    @PreAuthorize("hasAnyRole('USER','PM')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable Long id , @Valid @RequestBody ProjectUpdateDto dto) throws AccessDeniedException {
       return ResponseEntity.ok(projectService.updateProject(id,dto));
    }

    @Operation(summary = "Loyihani o'chirish (soft delete)", description = "Loyihani faqat flag bilan o'chiradi")
    @ApiResponse(responseCode = "204", description = "Loyiha o'chirildi")
    @ApiResponse(responseCode = "404", description = "Loyiha topilmadi")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletedProject(@PathVariable Long id) throws AccessDeniedException {
       projectService.deleteProject(id);
       return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Barcha loyihalarni olish", description = "Pagination bilan loyihalar ro'yxatini qaytaradi")
    @ApiResponse(responseCode = "200", description = "Loyihalar ro'yxati")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'PM')")
    @GetMapping
    public ResponseEntity<Page<ProjectResponseDto>> getAllProjects(@ParameterObject Pageable pageable) throws AccessDeniedException {
        return ResponseEntity.ok(projectService.getAllProjects(pageable));
    }

    @Operation(summary = "Bitta loyiha ma'lumotlarini olish", description = "ID bo'yicha loyiha detailini qaytaradi")
    @ApiResponse(responseCode = "200", description = "Loyiha topildi")
    @ApiResponse(responseCode = "404", description = "Loyiha topilmadi")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'PM')")
    @GetMapping("/project/{id}")
    public ResponseEntity<ProjectResponseDto> getById(@PathVariable Long id) throws AccessDeniedException {
       return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @Operation(summary = "Projectga PM biriktirish", description = "Faqat ADMIN bajara oladi")
    @ApiResponse(responseCode = "200", description = "PM muvaffaqiyatli biriktirildi")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}/assign-pm")
    public ResponseEntity<ProjectResponseDto> assignPm(@PathVariable Long id, @Valid @RequestBody AssignPmDto dto){
        return ResponseEntity.ok(projectService.assignPm(id,dto));
    }


}
