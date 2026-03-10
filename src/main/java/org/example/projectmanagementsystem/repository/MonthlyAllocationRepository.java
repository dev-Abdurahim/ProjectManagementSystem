package org.example.projectmanagementsystem.repository;

import org.example.projectmanagementsystem.entity.MonthlyAllocation;
import org.example.projectmanagementsystem.projection.MonthlyRevenueProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MonthlyAllocationRepository extends JpaRepository<MonthlyAllocation, Long>{

    // ✅ Line chart
    @Query("""
        SELECT MONTH(ma.monthYear) AS month,
               SUM(ma.amount) AS total
        FROM MonthlyAllocation ma
        WHERE YEAR(ma.monthYear) = :year
        AND ma.project.deleted = false
        GROUP BY MONTH(ma.monthYear)
        ORDER BY MONTH(ma.monthYear)
        """)
    List<MonthlyRevenueProjection> findMonthlyRevenueByYear(@Param("year") int year);

    // ✅ KPI actual revenue
    @Query("""
        SELECT COALESCE(SUM(ma.amount), 0)
        FROM MonthlyAllocation ma
        WHERE YEAR(ma.monthYear) = :year
        AND ma.project.deleted = false
        """)
    BigDecimal findActualRevenueByYear(@Param("year") int year);
}
