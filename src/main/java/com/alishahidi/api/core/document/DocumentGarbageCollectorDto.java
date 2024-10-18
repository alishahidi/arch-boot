package com.alishahidi.api.core.document;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentGarbageCollectorDto {
    Long id;
    String bucket;
    String path;
    Long expirationTime;
}
