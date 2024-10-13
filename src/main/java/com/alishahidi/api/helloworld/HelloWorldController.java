package com.alishahidi.api.helloworld;

import com.alishahidi.api.core.entity.BaseController;
import com.alishahidi.api.core.entity.BaseService;
import com.alishahidi.api.helloworld.dto.HelloWorldCreateDto;
import com.alishahidi.api.helloworld.dto.HelloWorldLoadDto;
import com.alishahidi.api.helloworld.dto.HelloWorldUpdateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello-world")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HelloWorldController extends BaseController<HelloWorldEntity, HelloWorldCreateDto, HelloWorldUpdateDto, HelloWorldLoadDto> {

    HelloWorldService helloWorldService;

    @Override
    protected BaseService<HelloWorldEntity, HelloWorldCreateDto, HelloWorldUpdateDto, HelloWorldLoadDto> getService() {
        return helloWorldService;
    }
}
