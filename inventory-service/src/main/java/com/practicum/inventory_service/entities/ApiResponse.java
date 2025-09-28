package com.practicum.inventory_service.entities;

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
