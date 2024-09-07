package com.alishahidi.api.core.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExecutorUtilsConfig {

    @Value("${config.executor.size}")
    private Integer sizeFromConfig;

    @PostConstruct
    private void init() {
        ExecutorUtils.size = sizeFromConfig;
    }
}
