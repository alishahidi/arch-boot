package com.alishahidi.api.core.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> implements Serializable {
    String type;
    int status;
    T data;
    LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .type(ApiResponseType.DATA.getName())
                .status(ApiResponseType.DATA.getStatus().value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .type(ApiResponseType.ERROR.getName())
                .status(status.value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> info(T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .type(ApiResponseType.INFO.getName())
                .status(status.value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> warning(T data) {
        return ApiResponse.<T>builder()
                .type(ApiResponseType.WARNING.getName())
                .status(ApiResponseType.WARNING.getStatus().value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
