package com.alishahidi.api.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum FileType {

    IMAGE("image"),
    VIDEO("video"),
    AUDIO("audio"),
    PDF("pdf"),
    WORD("word"),
    DOCUMENT("text"),
    UNKNOWN("unknown");

    String name;

    public static FileType fromMimeType(String mimeType) {
        if (mimeType == null) {
            return UNKNOWN;
        }

        String mainType = mimeType.split("/")[0];

        return switch (mainType) {
            case "image" -> IMAGE;
            case "video" -> VIDEO;
            case "audio" -> AUDIO;
            case "application", "text" -> {
                if (mimeType.equals("application/pdf")) {
                    yield PDF;
                } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                        || mimeType.equals("application/msword")) {
                    yield WORD;
                }
                yield DOCUMENT;
            }
            default -> UNKNOWN;
        };
    }
}
