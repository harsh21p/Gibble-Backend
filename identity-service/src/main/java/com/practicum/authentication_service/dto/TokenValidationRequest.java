package com.practicum.authentication_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenValidationRequest {
    @NotBlank
    private String token;
}
