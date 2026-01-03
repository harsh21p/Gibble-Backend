package com.practicum.inventory_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeRequest {

    @NotNull
    @Min(0)
    @Max(100)
    private Integer score;

    @Size(max = 512)
    private String feedback;
}
