package com.practicum.authentication_service.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    private String token;
}