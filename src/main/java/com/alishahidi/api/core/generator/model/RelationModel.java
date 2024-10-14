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

    public static RelationModel fromEntityModel(EntityModel entityModel, RelationType type, String mappedBy, String foreignKey, boolean required) {
        return RelationModel.builder()
                .type(type)
                .relatedEntityName(entityModel.getEntityName())
                .relatedEntityPackage(generatePackageName(entityModel.getEntityName()))
                .mappedBy(mappedBy)
                .foreignKey(foreignKey)
                .required(required)
                .build();
    }

    public static String generatePackageName(String entityName) {
        return (new BasePackage()).getBasePackage() + "." + entityName.toLowerCase();
    }

    public boolean hasMappedBy() {
        return mappedBy != null && !mappedBy.isEmpty();
    }
}
