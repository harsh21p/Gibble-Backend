package com.practicum.authentication_service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private final String accessToken;
    private final long expiresIn;
    private final String role;
    private final String accountId;
}
