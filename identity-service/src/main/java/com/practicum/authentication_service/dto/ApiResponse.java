package com.practicum.authentication_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final int status;
    private final boolean error;
    private final String message;
    private final T data;
}
