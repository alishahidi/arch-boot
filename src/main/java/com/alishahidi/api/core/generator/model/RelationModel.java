package com.alishahidi.api.core.generator.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.alishahidi.api.core.generator.BasePackage;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RelationModel {
    @JacksonXmlProperty(localName = "relationshipType")
    private RelationType type;

    @JacksonXmlProperty(localName = "relatedEntityName")
    private String relatedEntityName;

    private String relatedEntityPackage;

    @JacksonXmlProperty(localName = "mappedBy")
    private String mappedBy;

    @JacksonXmlProperty(localName = "foreignKey")
    private String foreignKey;

    @JacksonXmlProperty(localName = "required")
    private boolean required;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "document")
    private boolean document = false;

    public static String generatePackageName(String name) {
        if(name == null){
            return null;
        }
        return (new BasePackage()).getBasePackage() + "." + name.toLowerCase();
    }

    public boolean hasMappedBy() {
        return mappedBy != null && !mappedBy.isEmpty();
    }
}
