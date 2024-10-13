package com.alishahidi.api.helloworld;

import com.alishahidi.api.core.entity.BaseMapper;
import com.alishahidi.api.core.entity.BaseRepository;
import com.alishahidi.api.core.entity.BaseService;
import com.alishahidi.api.helloworld.dto.HelloWorldCreateDto;
import com.alishahidi.api.helloworld.dto.HelloWorldLoadDto;
import com.alishahidi.api.helloworld.dto.HelloWorldUpdateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HelloWorldService extends BaseService<HelloWorldEntity, HelloWorldCreateDto, HelloWorldUpdateDto, HelloWorldLoadDto> {

    HelloWorldRepository repository;
    HelloWorldMapper mapper = Mappers.getMapper(HelloWorldMapper.class);

    @Override
    protected BaseRepository<HelloWorldEntity> getRepository() {
        return repository;
    }

    @Override
    protected BaseMapper<HelloWorldEntity, HelloWorldCreateDto, HelloWorldUpdateDto, HelloWorldLoadDto> getMapper() {
        return mapper;
    }
}
