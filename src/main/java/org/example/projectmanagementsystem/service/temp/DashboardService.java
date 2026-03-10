package org.example.projectmanagementsystem.service.temp;

import org.example.projectmanagementsystem.dto.dashboard.DashboardOverviewDto;
import org.example.projectmanagementsystem.dto.dashboard.DashboardSummaryResponse;

import java.math.BigDecimal;

/**
 * Dashboard statistikasi va chart ma'lumotlarini hisoblash uchun service interfeysi.
 * Maqsad: Controller'ga to'liq yoki qisqa ma'lumot qaytarish.
 */
public interface DashboardService {

    DashboardOverviewDto getOverview(int year);

    DashboardSummaryResponse setKpiGoal(int year, BigDecimal targetRevenue);

}
