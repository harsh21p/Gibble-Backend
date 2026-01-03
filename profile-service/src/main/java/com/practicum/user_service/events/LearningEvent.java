package com.practicum.user_service.events;

public record LearningEvent(
        String accountId,
        String courseId,
        String type,
        String details
) { }
