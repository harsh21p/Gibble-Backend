package com.practicum.user_service.events;

import com.practicum.user_service.domain.RoleType;

public record AccountRegisteredEvent(
        String accountId,
        String email,
        RoleType role,
        String displayName
) { }
