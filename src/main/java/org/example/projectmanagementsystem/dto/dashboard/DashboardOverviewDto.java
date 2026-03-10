package org.example.projectmanagementsystem.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dashboard sahifasining to'liq ma'lumotlari uchun DTO.
 * Maqsad: Stats cards + barcha chartlar (line, pie, bar) uchun kerakli ma'lumotlarni bitta javobda qaytarish.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dashboardning to'liq ma'lumotlari (stats + chartlar)")
public class DashboardOverviewDto {

    @Schema(description = "Tanlangan yil", example = "2025")
    private Integer year;


    @Schema(description = "Umumiy statistika kartalari")
    private DashboardSummaryResponse summary;

    @Schema(description = "Oylar bo'yicha jami revenue (line chart)")
    private List<MonthlyAllocationDto> monthlyRevenue;

    @Schema(description = "Loyiha turi bo'yicha revenue ulushlari (pie chart)")
    private List<TypeRevenueShare> typeShares;


    @Schema(description = "Eng yuqori revenue'li top loyihalar")
    private List<TopProjectItem> topProjects;


    @Schema(description = "Eng ko'p kechikkan loyihalarni boshqarayotgan PM lar")
    private List<PmDelayItem> delayedPms;

}



