package com.practicum.authentication_service.dto;

import com.practicum.authentication_service.domain.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;

    @Size(max = 128)
    private String displayName;

    private RoleType role;
}
