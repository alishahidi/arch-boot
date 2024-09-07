package com.alishahidi.api.core.i18n;

import io.micrometer.common.lang.NonNullApi;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@NonNullApi
public class PrefixedMessageSource extends ReloadableResourceBundleMessageSource {

    List<String> ignoredPrefix = List.of("violations");  // Avoid adding prefix for validation messages

    @Override
    protected Properties loadProperties(Resource resource, String fileName) throws IOException {
        Properties properties = super.loadProperties(resource, fileName);
        String prefix = extractPrefixFromFileName(fileName);
        Properties prefixedProperties = new Properties();

        properties.forEach((key, value) -> {
            String prefixedKey = ignoredPrefix.contains(prefix) ? (String) key : prefix + "." + key;
            prefixedProperties.put(prefixedKey, value);
        });

        return prefixedProperties;
    }

    private String extractPrefixFromFileName(String fileName) {
        String[] parts = fileName.split("/");
        if (parts.length > 2) {
            return parts[parts.length - 2];
        }
        return "";
    }
}
