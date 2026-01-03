package com.practicum.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequest {

    @NotBlank
    @Size(max = 180)
    private String title;

    @Size(max = 1024)
    private String description;

    @Size(max = 64)
    private String category;

    @Size(max = 32)
    private String level;
}
