package org.example.projectmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagementsystem.entity.base.BaseEntity;
import org.example.projectmanagementsystem.enums.ProjectStatus;
import org.example.projectmanagementsystem.enums.ProjectType;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "projects")
@Where(clause = "deleted = false")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProjectType type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal revenue;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "working_days", nullable = false)
    private Integer workingDays;

    @Column(name = "project_manager", length = 100)
    private String projectManager;

    @Column(name = "plan_percentage", precision = 5, scale = 2)
    private BigDecimal planPercentage = BigDecimal.ZERO;

    @Column(name = "actual_percentage", precision = 5, scale = 2)
    private BigDecimal actualPercentage = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProjectStatus status = ProjectStatus.PLANNED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthlyAllocation> monthlyAllocations = new ArrayList<>();

    // Minimal biznes helper (entity ichida faqat oddiy logic bo'lishi kerak)
    public boolean isOverdue() {
        return endDate.isBefore(LocalDate.now()) && status != ProjectStatus.FINISHED;
    }
}
