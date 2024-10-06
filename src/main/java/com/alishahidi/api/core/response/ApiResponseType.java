package com.alishahidi.api.core.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ApiResponseType {

    INFO("info", HttpStatus.OK),
    WARNING("warning", HttpStatus.BAD_REQUEST),
    ERROR("error", HttpStatus.INTERNAL_SERVER_ERROR),
    DATA("data", HttpStatus.OK);

    String name;
    HttpStatus status;
}
