package com.alishahidi.api.core.i18n;

import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class I18nUtil {

    MessageSource messageSource;

    @Resource(name = "localeHolder")
    LocaleHolder localeHolder;

    public String getMessage(String code, String... args) {
        return messageSource.getMessage(code, args, localeHolder.getCurrentLocale());
    }
}
