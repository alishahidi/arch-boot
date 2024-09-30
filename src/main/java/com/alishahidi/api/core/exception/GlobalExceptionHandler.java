package com.alishahidi.api.core.exception;

import com.alishahidi.api.core.i18n.I18nUtil;
import com.alishahidi.api.core.response.Response;
import com.alishahidi.api.core.response.ResponseType;
import com.alishahidi.api.core.s3.exception.BucketPutException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {

    I18nUtil i18nUtil;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Response<String>> handleAppException(AppException ex) {
        return new ResponseEntity<>(
                Response.error(i18nUtil.getMessage(ex.getKey()), ex.getStatus()),
                ex.getStatus()
        );
    }

    @ExceptionHandler(BucketPutException.class)
    public ResponseEntity<Response<String>> handleBucketPutException(BucketPutException ex) {
        return new ResponseEntity<>(
                Response.error(i18nUtil.getMessage("error.bucket.put"), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // FallBack
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<String> handleGeneralException(Exception ex) {
        return Response.error(i18nUtil.getMessage("server.error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}