package org.example.projectmanagementsystem.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagementsystem.dto.dashboard.DashboardOverviewDto;
import org.example.projectmanagementsystem.dto.dashboard.DashboardSummaryResponse;
import org.example.projectmanagementsystem.service.temp.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Tag(name = "Dashboard", description = "Dashboard statistikasi va chart ma'lumotlari")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Dashboard to'liq ma'lumotlari")
    @PreAuthorize("hasAnyRole('ADMIN', 'PM', 'USER')")
    @GetMapping
    public ResponseEntity<DashboardOverviewDto> getOverview(@RequestParam(required = false) Integer year){
        int targetYear = (year != null) ? year : LocalDate.now().getYear();
        return ResponseEntity.ok(dashboardService.getOverview(targetYear));

    }

    @Operation(summary = "KPI maqsad belgilash — faqat ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/kpi")
    public ResponseEntity<DashboardSummaryResponse> setKpiGoal(@RequestParam int year, @RequestParam BigDecimal targetRevenue){
        return ResponseEntity.ok(dashboardService.setKpiGoal(year,targetRevenue));

    }

}
