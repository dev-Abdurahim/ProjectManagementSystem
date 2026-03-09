package org.example.projectmanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.projectmanagementsystem.dto.dashboard.MonthlyAllocationDto;
import org.example.projectmanagementsystem.enums.ProjectStatus;
import org.example.projectmanagementsystem.enums.ProjectType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Loyiha haqida batafsil javob modeli")
public class ProjectResponseDto {

    @Schema(description = "Loyiha ID", example = "42")
    private Long id;

    @Schema(description = "Loyiha nomi", example = "Mobile Banking App")
    private String projectName;

    @Schema(description = "Loyiha turi", example = "SI")
    private ProjectType type;

    @Schema(description = "Umumiy revenue", example = "50000.00")
    private BigDecimal revenue;

    @Schema(description = "Boshlanish sanasi", example = "2025-02-01")
    private LocalDate startDate;

    @Schema(description = "Tugash sanasi", example = "2025-12-31")
    private LocalDate endDate;

    @Schema(description = "Ish kunlari soni (avto hisoblangan)", example = "220")
    private Integer workingDays;

    @Schema(description = "Loyiha menejeri", example = "David Johnson")
    private String projectManager;

    @Schema(description = "Rejalashtirilgan progress", example = "60.00")
    private BigDecimal planPercentage;

    @Schema(description = "Haqiqiy progress", example = "45.50")
    private BigDecimal actualPercentage;

    @Schema(description = "Loyiha holati", example = "ONGOING")
    private ProjectStatus status;

    @Schema(description = "Loyihani yaratgan foydalanuvchi", example = "john_doe")
    private String ownerUsername;

    @Schema(description = "Oylik revenue taqsimoti", example = "[{monthYear: '2025-02', amount: 4166.67}, ...]")
    private List<MonthlyAllocationDto> monthlyAllocations;

    @Schema(description = "Yaratilgan sana")
    private LocalDate createdAt;

    @Schema(description = "Oxirgi o'zgartirilgan sana")
    private LocalDate updatedAt;
}
