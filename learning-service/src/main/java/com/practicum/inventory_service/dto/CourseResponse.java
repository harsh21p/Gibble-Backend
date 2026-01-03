package com.practicum.inventory_service.dto;

import com.practicum.inventory_service.domain.CourseStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseResponse {
    private final String id;
    private final String code;
    private final String title;
    private final String description;
    private final String teacherId;
    private final String category;
    private final String level;
    private final CourseStatus status;
}
