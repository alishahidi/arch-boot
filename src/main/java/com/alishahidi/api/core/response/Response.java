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
public class Response<T> implements Serializable {
    String type;
    int status;
    T data;
    LocalDateTime timestamp;

    public static <T> Response<T> success(T data) {
        return Response.<T>builder()
                .type(ResponseType.DATA.getName())
                .status(ResponseType.DATA.getStatus().value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> Response<T> error(T data, HttpStatus status) {
        return Response.<T>builder()
                .type(ResponseType.ERROR.getName())
                .status(status.value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> Response<T> info(T data, HttpStatus status) {
        return Response.<T>builder()
                .type(ResponseType.INFO.getName())
                .status(status.value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> Response<T> warning(T data) {
        return Response.<T>builder()
                .type(ResponseType.WARNING.getName())
                .status(ResponseType.WARNING.getStatus().value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
