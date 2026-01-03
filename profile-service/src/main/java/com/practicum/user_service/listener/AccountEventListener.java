package com.practicum.user_service.listener;

import com.practicum.user_service.events.AccountRegisteredEvent;
import com.practicum.user_service.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountEventListener {

    private final ProfileService profileService;

    @KafkaListener(
            topics = "identity.accounts",
            groupId = "profile-service",
            properties = {"spring.json.value.default.type=com.practicum.user_service.events.AccountRegisteredEvent"}
    )
    public void handleAccount(AccountRegisteredEvent event) {
        log.info("Creating profile for account {}", event.accountId());
        profileService.ensureProfile(event);
    }
}
