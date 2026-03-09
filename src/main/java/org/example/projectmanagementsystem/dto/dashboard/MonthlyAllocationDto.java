package org.example.projectmanagementsystem.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAllocationDto {

    @Schema(description = "Oy va yil", example = "2025-04")
    private YearMonth monthYear;

    @Schema(description = "Shu oy uchun rejalashtirilgan miqdor", example = "16666.67")
    private BigDecimal amount;
}
