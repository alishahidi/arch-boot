package com.alishahidi.api.core.i18n;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    public PrefixedMessageSource messageSource() {
        PrefixedMessageSource messageSource = new PrefixedMessageSource();
        messageSource.setBasenames(getBases().toArray(new String[0]));
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    private List<String> getBases() {
        List<String> bases = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            Resource[] resources = resolver.getResources("classpath*:properties/**/*.properties");

            for (Resource resource : resources) {
                String filename = resource.getURI().toString();

                if (filename.endsWith(".properties")) {
                    String baseName = filename
                            .replaceFirst(".*/properties/", "classpath:properties/")
                            .replace(".properties", "")
                            .replaceAll("%20", " "); // Handle spaces in paths

                    bases.add(baseName);
                }
            }
        } catch (IOException e) {
            bases.add("classpath:properties/messages");
        }

        return bases;
    }
}

