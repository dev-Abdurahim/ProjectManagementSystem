package org.example.projectmanagementsystem.repository;

import org.example.projectmanagementsystem.entity.Project;


import org.example.projectmanagementsystem.projection.DelayedPmProjection;
import org.example.projectmanagementsystem.projection.TopProjectProjection;
import org.example.projectmanagementsystem.projection.TypeRevenueProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    Page<Project> findAllByOwnerId(Long ownerId, Pageable pageable);

    Optional<Project> findByIdAndOwnerId(Long id, Long ownerId);

    // PM: o'ziga biriktirilgan loyihalar (projectManager = username)
    Page<Project> findAllByProjectManager(String username, Pageable pageable);

    Optional<Project> findByIdAndProjectManager(Long id, String username);


    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.monthlyAllocations WHERE p.deleted = false")
    Page<Project> findAllProjectProjections(Pageable pageable);


    @Query("SELECT COUNT(p) FROM Project p WHERE p.deleted = false")
    Integer countTotal();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'DELAYED' AND p.deleted = false")
    Integer countDelayed();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'ONGOING' AND p.deleted = false")
    Integer countOngoing();

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'DROPPED' AND p.deleted = false")
    Integer countDropped();

    // ✅ Business type donut chart
    @Query("""
        SELECT p.type AS type,
               SUM(p.revenue) AS total
        FROM Project p
        WHERE YEAR(p.startDate) = :year
        AND p.deleted = false
        GROUP BY p.type
        ORDER BY total DESC
        """)
    List<TypeRevenueProjection> findRevenueByType(@Param("year") int year);


    // ✅ Top 4 projects daromadi yuqori bolgan
    @Query("""
        SELECT p.name AS projectName,
               p.revenue AS revenue
        FROM Project p
        WHERE YEAR(p.startDate) = :year
        AND p.deleted = false
        ORDER BY p.revenue DESC
        LIMIT 4
        """)
    List<TopProjectProjection> findTop4ProjectsByRevenue(@Param("year") int year);


    // ✅ Delayed PM lar — top 5
    @Query("""
        SELECT p.projectManager AS pmName,
               COUNT(p) AS delayedCount,
               AVG(p.planPercentage - p.actualPercentage) AS averageDelayPercent
        FROM Project p
        WHERE p.projectManager IS NOT NULL
        AND p.planPercentage > p.actualPercentage
        AND p.deleted = false
        GROUP BY p.projectManager
        ORDER BY averageDelayPercent DESC
        LIMIT 5
        """)
    List<DelayedPmProjection> findDelayedPms();


}

