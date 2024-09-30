package com.alishahidi.api.core.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ExceptionTemplate {

    FILE_PROCESS("error.file.process", HttpStatus.BAD_REQUEST),
    IMAGE_TYPE_ERROR("error.image.type", HttpStatus.BAD_REQUEST),
    PERSON_NOT_FOUND("error.person.not.found", HttpStatus.NOT_FOUND);

    String key;
    HttpStatus status;
}