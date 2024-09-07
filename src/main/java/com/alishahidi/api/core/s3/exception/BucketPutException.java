package com.alishahidi.api.core.s3.exception;

public class BucketPutException extends RuntimeException {
    public BucketPutException(String message) {
        super(message);
    }

    public BucketPutException(String message, Throwable cause) {
        super(message, cause);
    }

}
