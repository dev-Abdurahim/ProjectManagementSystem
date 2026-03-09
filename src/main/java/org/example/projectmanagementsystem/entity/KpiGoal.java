package org.example.projectmanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import org.example.projectmanagementsystem.entity.base.BaseEntity;

import java.math.BigDecimal;

/**
 * Har yil uchun umumiy daromad maqsadi (Dashboard KPI uchun)
 */

@Entity
@Table(name = "kpi_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KpiGoal extends BaseEntity {

    @Column(nullable = false)
    private Integer year;

    @Column(name = "target_revenue", nullable = false, precision = 15, scale = 2)
    private BigDecimal targetRevenue;
}
