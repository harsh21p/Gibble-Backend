package com.practicum.inventory_service.events;

public record LearningEvent(
        String accountId,
        String courseId,
        String type,
        String details
) { }
