package org.example.projectmanagementsystem.projection;

import java.math.BigDecimal;

public interface MonthlyRevenueProjection {
    Integer getMonth();
    BigDecimal getTotal();
}
