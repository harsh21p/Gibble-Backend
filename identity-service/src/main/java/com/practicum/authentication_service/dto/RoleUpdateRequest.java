package com.practicum.authentication_service.dto;

import com.practicum.authentication_service.domain.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUpdateRequest {
    @NotBlank
    private String accountId;

    @NotNull
    private RoleType role;
}
