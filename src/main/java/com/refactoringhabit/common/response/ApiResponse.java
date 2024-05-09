package com.refactoringhabit.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private int status;
    private T data;

    public ApiResponse() {}

    public ApiResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), data);
    }

    public static ApiResponse<String> noContent() {
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "no content");
    }
}