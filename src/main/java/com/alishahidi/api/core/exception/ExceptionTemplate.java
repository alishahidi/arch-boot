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
    PERSON_NOT_FOUND("error.person.not.found", HttpStatus.NOT_FOUND),
    S3_SCOPE_ERROR("error.s3.scope.error", HttpStatus.BAD_REQUEST),
    FILE_MIME_TYPE("error.file.type", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("entity.not.found", HttpStatus.NOT_FOUND),
    TOKEN_NOT_VALID("token.not.valid", HttpStatus.UNAUTHORIZED),
    CONTRACT_DOCUMENT_PROCESS_ERROR("document.process.error", HttpStatus.BAD_REQUEST),
    PDF_PAGE_ERROR_OR_NOT_FOUND("pdf.page.error", HttpStatus.INTERNAL_SERVER_ERROR);

    String key;
    HttpStatus status;
}
