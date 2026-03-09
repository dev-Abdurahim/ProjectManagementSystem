package org.example.projectmanagementsystem.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class TypeRevenueShare {
    private String type;           // SI, SM, etc.
    private BigDecimal percentage; // 65.0
    private BigDecimal total;      // 260000.00
}
