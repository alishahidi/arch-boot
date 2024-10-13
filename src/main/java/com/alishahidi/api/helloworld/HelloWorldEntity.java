package com.alishahidi.api.helloworld;

import com.alishahidi.api.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "hello_worlds"
)
public class HelloWorldEntity extends BaseEntity {
    String name;
    String message;

}
