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
public  class PmDelayItem {
    private String pmName;
    private Integer delayedCount;
    private BigDecimal averageDelayPercent; // masalan -33.0
}
