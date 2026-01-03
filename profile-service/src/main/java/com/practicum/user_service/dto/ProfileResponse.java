package com.practicum.user_service.dto;

import com.practicum.user_service.domain.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ProfileResponse {
    private final String accountId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String headline;
    private final String bio;
    private final String timezone;
    private final RoleType role;
    private final Map<String, String> preferences;
}
