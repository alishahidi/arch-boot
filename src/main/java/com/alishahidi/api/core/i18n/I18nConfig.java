package com.alishahidi.api.core.i18n;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class I18nConfig implements WebMvcConfigurer {

    LocaleInterceptor localeInterceptor;

    @Bean
    public AcceptHeaderLocaleResolver acceptHeaderLocaleResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public LocaleHolder localeHolder() {
        return new LocaleHolder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(localeInterceptor);
    }

    @Bean
    public MessageSource messageSource() {
        PrefixedMessageSource messageSource = new PrefixedMessageSource();

        messageSource.setBasenames(getBases().toArray(new String[0]));
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

    private List<String> getBases() {
        List<String> bases = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/properties"))) {
            paths.filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".properties"))
                    .forEach(path -> {
                        String relativePath = path.toString().replace("src/main/resources/", "classpath:");
                        String baseName = relativePath.replace(".properties", "");
                        bases.add(baseName);
                    });
        } catch (Exception e) {
            bases.add("classpath:properties/messages");
        }
        return bases;
    }
}
