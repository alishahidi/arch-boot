package com.alishahidi.api.core.generator.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FieldModel {
    @JacksonXmlProperty(localName = "name")
    String name;

    @JacksonXmlProperty(localName = "type")
    FieldType type;

    @JacksonXmlProperty(localName = "required")
    boolean required;
}