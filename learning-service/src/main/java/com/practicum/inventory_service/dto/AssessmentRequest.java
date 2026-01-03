package com.practicum.inventory_service.dto;

import com.practicum.inventory_service.domain.AssessmentType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AssessmentRequest {

    @NotBlank
    private String title;

    @Size(max = 512)
    private String instructions;

    @NotNull
    private AssessmentType type;

    @FutureOrPresent
    private Instant dueDate;

    private Integer maxScore;
}
