package com.alishahidi.api.core.generator.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntityModel {
    String entityName;
    List<FieldModel> fields;
    List<RelationModel> relations;
}
