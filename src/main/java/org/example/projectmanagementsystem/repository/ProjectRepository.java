package org.example.projectmanagementsystem.repository;

import jakarta.transaction.Transactional;
import org.example.projectmanagementsystem.entity.Project;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    Page<Project> findAllByOwnerId(Long ownerId, Pageable pageable);

    Optional<Project> findByIdAndOwnerId(Long id, Long ownerId);

    // PM: o'ziga biriktirilgan loyihalar (projectManager = username)
    Page<Project> findAllByProjectManager(String username, Pageable pageable);

    Optional<Project> findByIdAndProjectManager(Long id, String username);


//    long countByStatus(ProjectStatus status);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.monthlyAllocations WHERE p.deleted = false")
    Page<Project> findAllProjectProjections(Pageable pageable);
}

