package com.den.pos.web.dto;

public record ApiResponse<T>(boolean success, String message, T data) {

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    public static ApiResponse<Void> successMessage(String message) {
        return new ApiResponse<>(true, message, null);
    }
}
