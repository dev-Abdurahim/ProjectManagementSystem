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
public class MonthlyAllocationDto {

    @Schema(description = "Oy va yil", example = "2025-04")
    private String monthYear;

    @Schema(description = "Shu oy uchun projectni umumiy miqdor", example = "16666.67")
    private BigDecimal amount;
}
