package com.practicum.authentication_service.events;

import com.practicum.authentication_service.domain.RoleType;

public record AccountRegisteredEvent(
        String accountId,
        String email,
        RoleType role,
        String displayName
) { }
