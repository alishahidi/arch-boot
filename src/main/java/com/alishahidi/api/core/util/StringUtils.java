package com.alishahidi.api.core.util;

import lombok.experimental.UtilityClass;

import java.text.Normalizer;

@UtilityClass
public class StringUtils {
    public String normalize(String inputName) {
        // Convert to lowercase for consistency
        String str = inputName.toLowerCase();

        // Remove accents and diacritics (e.g., "é" -> "e")
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Replace spaces with hyphens (or underscores if preferred)
        str = str.replaceAll("\\s+", "-");

        // Remove all non-alphanumeric characters except hyphens and underscores
        str = str.replaceAll("[^a-z0-9-_]", "");

        // Limit length if necessary (optional)
        int maxLength = 255;  // Adjust this limit based on your needs
        if (str.length() > maxLength) {
            str = str.substring(0, maxLength);
        }

        return str;
    }

    public String toUpperCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
}
