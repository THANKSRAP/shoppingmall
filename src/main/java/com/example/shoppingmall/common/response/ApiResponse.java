package com.example.shoppingmall.common.response;

import com.example.shoppingmall.common.exception.ErrorCode;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "요청 성공", data);
    }

    public static ApiResponse<?> error(int status, String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static ApiResponse<?> error(ErrorCode code) {
        return new ApiResponse<>(false, code.getMessage(), null);
    }

    // getter 생략 가능 (Lombok @Getter 사용 가능)
}
