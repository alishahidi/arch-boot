package com.alishahidi.api.core.document;

import com.alishahidi.api.core.s3.S3Scope;
import com.alishahidi.api.core.util.FileType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "documents"
)
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String path;
    String name;

    Long size;
    @Enumerated(EnumType.STRING)
    FileType type;
    String mimeType;
    String extension;
    String hash;
    @Enumerated(EnumType.STRING)
    S3Scope scope;

    @CreationTimestamp
    Date createdAt;
    @UpdateTimestamp
    Date updatedAt;
}
