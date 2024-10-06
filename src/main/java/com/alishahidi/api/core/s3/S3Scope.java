package com.alishahidi.api.core.s3;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum S3Scope {

    PRIVATE("private-contract"),
    PUBLIC("public"),
    UNKNOWN("unknown");

    String bucket;

    public static S3Scope fromScope(String scope) {
        if (scope == null) {
            return UNKNOWN;
        }

        return switch (scope) {
            case "contract-private" -> PRIVATE;
            case "public" -> PUBLIC;
            default -> UNKNOWN;
        };
    }
}