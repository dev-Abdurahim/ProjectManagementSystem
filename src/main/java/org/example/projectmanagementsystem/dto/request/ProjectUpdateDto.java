package org.example.projectmanagementsystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectmanagementsystem.enums.ProjectStatus;
import org.example.projectmanagementsystem.enums.ProjectType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateDto {

    @Size(min = 3, max = 150, message = "Loyiha nomi 3-150 belgi oralig'ida bo'lishi kerak")
    @Schema(description = "Loyiha nomi (ixtiyoriy)", example = "Updated Mobile Banking App")
    private String projectName;

    @Schema(description = "Loyiha turi (ixtiyoriy)", example = "SI")
    private ProjectType type;

    @Positive(message = "Revenue musbat son bo'lishi kerak")
    @Schema(description = "Yangilangan revenue (ixtiyoriy)", example = "75000.00")
    private BigDecimal revenue;

    @PastOrPresent(message = "Boshlanish sanasi o'tmishda yoki bugun bo'lishi mumkin")
    @Schema(description = "Boshlanish sanasi (ixtiyoriy)", example = "2025-02-01")
    private LocalDate startDate;

    @FutureOrPresent(message = "Tugash sanasi kelajakda bo'lishi kerak")
    @Schema(description = "Tugash sanasi (ixtiyoriy)", example = "2025-12-31")
    private LocalDate endDate;

    @Schema(description = "Loyiha holati (ixtiyoriy)", example = "ONGOING")
    private ProjectStatus status;

    @DecimalMin(value = "0.0", message = "Plan % 0 dan kichik bo'lmasligi kerak")
    @DecimalMax(value = "100.0", message = "Plan % 100 dan katta bo'lmasligi kerak")
    @Schema(description = "Rejalashtirilgan progress foizi", example = "45.50")
    private BigDecimal planPercentage;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    @Schema(description = "Haqiqiy progress foizi", example = "32.75")
    private BigDecimal actualPercentage;
}
