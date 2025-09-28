package com.practicum.user_service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetails {
    private String userId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String username;
}
