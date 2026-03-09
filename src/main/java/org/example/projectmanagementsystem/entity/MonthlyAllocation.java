package org.example.projectmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.projectmanagementsystem.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "monthly_allocations")
// Projectni oyli daromadi
public class MonthlyAllocation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "month_year", nullable = false)
    private LocalDate monthYear;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
}
