package com.practicum.user_service.listener;

import com.practicum.user_service.events.LearningEvent;
import com.practicum.user_service.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LearningEventListener {

    private final ActivityLogService activityLogService;

    @KafkaListener(
            topics = "learning.events",
            groupId = "profile-service",
            properties = {"spring.json.value.default.type=com.practicum.user_service.events.LearningEvent"}
    )
    public void handleLearningEvent(LearningEvent event) {
        log.info("Recording learning event {} for account {}", event.type(), event.accountId());
        activityLogService.record(event);
    }
}
