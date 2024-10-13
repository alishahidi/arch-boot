package com.alishahidi.api.helloworld.dto;

import com.alishahidi.api.core.entity.BaseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HelloWorldLoadDto extends BaseDto {
    String name;
    String message;
}
