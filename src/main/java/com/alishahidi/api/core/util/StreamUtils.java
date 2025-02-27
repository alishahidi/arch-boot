package com.alishahidi.api.core.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@UtilityClass
public class StreamUtils {
    public <O> Optional<O> findObject(List<O> objects, Predicate<O> filter) {
        return objects.stream()
                .filter(filter)
                .findFirst();
    }
}
