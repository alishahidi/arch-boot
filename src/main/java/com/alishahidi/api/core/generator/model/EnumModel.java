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
public class EnumModel {
    @JacksonXmlProperty(localName = "name")
    String name;

    @JacksonXmlProperty(localName = "value")
    String value;
}