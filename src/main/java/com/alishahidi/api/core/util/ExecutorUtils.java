package com.alishahidi.api.core.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExecutorUtils {

    static Integer size;

    public Executor fixedThreadPool() {
        return Executors.newFixedThreadPool(size);
    }

}
