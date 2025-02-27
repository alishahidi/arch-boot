package com.alishahidi.api.core.generator.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

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

    @JacksonXmlElementWrapper(localName = "enums")
    @JacksonXmlProperty(localName = "enum")
    List<EnumModel> enums = new ArrayList<>();
}