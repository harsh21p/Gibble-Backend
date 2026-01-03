package com.practicum.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ProfileUpdateRequest {

    private String accountId;

    private String email;

    @Size(max = 64)
    private String firstName;

    @Size(max = 64)
    private String lastName;

    @Size(max = 180)
    private String headline;

    @Size(max = 512)
    private String bio;

    private String timezone;

    private Map<String, String> preferences;
}
