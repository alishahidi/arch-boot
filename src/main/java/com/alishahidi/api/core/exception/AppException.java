package com.alishahidi.api.core.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppException extends RuntimeException {

    String key;
    HttpStatus status;

    public AppException(ExceptionTemplate template) {
        super(template.getKey());  // Use the key for localization
        this.key = template.getKey();
        this.status = template.getStatus();
    }
}