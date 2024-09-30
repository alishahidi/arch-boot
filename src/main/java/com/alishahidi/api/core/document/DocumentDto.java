package com.alishahidi.api.core.document;

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

    String type;
    String extension;
    Long size;
    String path;

    Date createdAt;
    Date updatedAt;
}
