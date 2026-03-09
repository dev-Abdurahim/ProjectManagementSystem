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
public class ProjectCreateDto {

    @NotBlank(message = "Loyiha nomi majburiy")
    @Size(min = 3, max = 150, message = "Loyiha nomi 3-150 belgi oralig'ida bo'lishi kerak")
    @Schema(description = "Loyiha nomi", example = "Mobile Banking App", required = true)
    private String projectName;

    @NotNull(message = "Loyiha turi majburiy")
    @Schema(description = "Loyiha turi (SI, SM, SIP, OTHER)", example = "SI", required = true)
    private ProjectType type;

    @NotNull(message = "Revenue majburiy")
    @Positive(message = "Revenue musbat son bo'lishi kerak")
    @Schema(description = "Umumiy revenue (USD yoki boshqa valyutada)", example = "50000.00", required = true)
    private BigDecimal revenue;

    @NotNull(message = "Boshlanish sanasi majburiy")
    @PastOrPresent(message = "Boshlanish sanasi o'tmishda yoki bugun bo'lishi mumkin")
    @Schema(description = "Loyiha boshlanish sanasi", example = "2025-02-01", required = true)
    private LocalDate startDate;

    @NotNull(message = "Tugash sanasi majburiy")
    @FutureOrPresent(message = "Tugash sanasi kelajakda bo'lishi kerak")
    @Schema(description = "Loyiha tugash sanasi", example = "2025-12-31", required = true)
    private LocalDate endDate;

}
