package com.alishahidi.api.core.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContext {
    private static ApplicationContext context;

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        SpringContext.context = context;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}
