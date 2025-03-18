package com.mobieslow.paymentservice.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ApiResponse<T> extends ResponseEntity<Result<T, String>> {
    private ApiResponse(Result<T, String> body, HttpStatusCode status) {
        super(body, status);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(Result.ok(data), HttpStatus.OK);
    }

    public static <T> ApiResponse<T> badRequest(String error) {
        return new ApiResponse<>(Result.err(error), HttpStatus.BAD_REQUEST);
    }

    public static <T> ApiResponse<T> internalServerError(String error) {
        return new ApiResponse<>(Result.err(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
