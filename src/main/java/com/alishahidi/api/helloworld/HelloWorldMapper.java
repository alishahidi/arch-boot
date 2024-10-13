package com.alishahidi.api.helloworld;

import com.alishahidi.api.core.entity.BaseMapper;
import com.alishahidi.api.helloworld.dto.HelloWorldCreateDto;
import com.alishahidi.api.helloworld.dto.HelloWorldLoadDto;
import com.alishahidi.api.helloworld.dto.HelloWorldUpdateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HelloWorldMapper extends BaseMapper<HelloWorldEntity, HelloWorldCreateDto, HelloWorldUpdateDto, HelloWorldLoadDto> {
}
