package com.alishahidi.api.core.entity;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface BaseMapper<T extends BaseEntity, C extends BaseDto, U extends BaseDto, L extends BaseDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    @Mapping(target = "deletedAt", ignore = true)
    T create(C createDto);

    T entity(L createDto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    @Mapping(target = "deletedAt", ignore = true)
    void update(U updateDto, @MappingTarget T target);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    @Mapping(target = "deletedAt", ignore = true)
    void pureUpdate(T entity, @MappingTarget T target);

    L load(T entity);
}