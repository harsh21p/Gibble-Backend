package com.practicum.api_gateway.models;

import lombok.Data;

@Data
public class ApiResponse<T> {
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

     public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
