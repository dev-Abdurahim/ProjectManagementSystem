package org.example.projectmanagementsystem.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dashboardning asosiy statistika kartalari uchun qisqa javob modeli")
public class DashboardSummaryResponse {

    @Schema(description = "Jami loyihalar soni", example = "45")
    private Integer totalProjects;

    @Schema(description = "Kechikkan loyihalar soni", example = "4")
    private Integer delayedProjects;

    @Schema(description = "Davom etayotgan loyihalar soni", example = "12")
    private Integer ongoingProjects;

    @Schema(description = "To'xtatilgan (dropped) loyihalar soni", example = "3")
    private Integer droppedProjects;

    private BigDecimal actualRevenue;
    private BigDecimal targetRevenue;
    private BigDecimal achievementRate;
    private BigDecimal difference;

}
