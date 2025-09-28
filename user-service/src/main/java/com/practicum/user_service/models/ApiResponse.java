package com.practicum.user_service.models;

import lombok.Data;

@Data
public class ApiResponse<T>  {
    private Integer status;
    private String message;
    private T data;
    private boolean error;

    public ApiResponse(Integer status, Boolean error, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
    }
}
