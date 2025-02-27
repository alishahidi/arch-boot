package com.alishahidi.api.core.i18n;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Component
public class PrefixedMessageSource extends ReloadableResourceBundleMessageSource {

    private static final List<String> IGNORED_PREFIXES = List.of("violations", "core", "exception");

    public PrefixedMessageSource() {
        setDefaultEncoding("UTF-8");
        setCacheSeconds(-1); // Cache messages indefinitely for best performance
    }

    @Override
    protected Properties loadProperties(Resource resource, String fileName) throws IOException {
        Properties properties = super.loadProperties(resource, fileName);

        if (properties.isEmpty()) {
            return properties; // Skip processing if empty
        }

        String prefix = extractPrefixFromFileName(fileName);
        if (IGNORED_PREFIXES.contains(prefix)) {
            return properties; // Skip adding prefix for ignored categories
        }

        Properties prefixedProperties = new Properties();
        properties.forEach((key, value) -> prefixedProperties.put(prefix + "." + key, value));

        return prefixedProperties;
    }

    private String extractPrefixFromFileName(String fileName) {
        String[] parts = fileName.split("/");
        return parts.length > 2 ? parts[parts.length - 2] : "";
    }
}
