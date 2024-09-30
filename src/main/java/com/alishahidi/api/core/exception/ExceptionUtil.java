package com.alishahidi.api.core.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionUtil {

    public AppException make(ExceptionTemplate template) {
        return new AppException(template);
    }
}
