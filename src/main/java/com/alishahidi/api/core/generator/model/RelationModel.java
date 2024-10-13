package com.alishahidi.api.core.generator.model;

import com.alishahidi.api.core.generator.BasePackage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@AllArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RelationModel {
    String relationshipType;        // OneToOne, OneToMany, ManyToOne, ManyToMany
    String relatedEntityName;       // Name of the related entity
    String relatedEntityPackage;    // Package name of the related entity
    String mappedBy;                // Field used in the relationship (for bidirectional relationships)
    String foreignKey;              // Foreign key column for ManyToOne relations

    // Automatically create a relationship based on the EntityModel details
    public static RelationModel fromEntityModel(EntityModel entityModel, String relationshipType, String mappedBy, String foreignKey) {
        return RelationModel.builder()
                .relationshipType(relationshipType)
                .relatedEntityName(entityModel.getEntityName())
                .relatedEntityPackage((new BasePackage()).getBasePackage() + "." + entityModel.getEntityName().toLowerCase())
                .mappedBy(mappedBy)
                .foreignKey(foreignKey)
                .build();
    }

}