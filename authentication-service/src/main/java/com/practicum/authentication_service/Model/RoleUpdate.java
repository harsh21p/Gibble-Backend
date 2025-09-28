package com.practicum.authentication_service.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleUpdate {
    private String role;
    private String department;
    private String region;
    private String email;
}
