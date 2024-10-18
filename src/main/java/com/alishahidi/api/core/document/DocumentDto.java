package com.alishahidi.api.core.document;

import com.alishahidi.api.core.s3.S3Scope;
import com.alishahidi.api.core.util.FileType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentDto {
    Long id;

    String path;

    Long size;
    FileType type;
    String mimeType;
    String extension;
    S3Scope scope;

    Date createdAt;
    Date updatedAt;
}
