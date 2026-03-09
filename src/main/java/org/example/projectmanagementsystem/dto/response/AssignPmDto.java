package org.example.projectmanagementsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignPmDto {

    @NotNull(message = "User ID majburiy")
    @Schema(description = "PM rolidagi user ID si", example = "3")
    private Long pmUserId;
}
