package com.practicum.user_service.service;

import com.practicum.user_service.domain.LearningActivity;
import com.practicum.user_service.events.LearningEvent;
import com.practicum.user_service.repository.LearningActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final LearningActivityRepository repository;

    public void record(LearningEvent event) {
        repository.save(LearningActivity.builder()
                .accountId(event.accountId())
                .courseId(event.courseId())
                .eventType(event.type())
                .details(event.details())
                .build());
    }
}
