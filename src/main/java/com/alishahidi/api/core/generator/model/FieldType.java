package com.alishahidi.api.core.generator.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum FieldType {
    STRING("String", null),
    INTEGER("Integer", null),
    LONG("Long", null),
    BOOLEAN("Boolean", null),
    DATE("Date", "java.util.Date"),
    BIG_DECIMAL("BigDecimal", "java.math.BigDecimal"),
    DOUBLE("Double", null),
    LOCAL_DATE("LocalDate", "java.time.LocalDate"),
    LOCAL_DATE_TIME("LocalDateTime", "java.time.LocalDateTime");

    String type;
    String importPath;

    public boolean requiresImport() {
        return importPath != null;
    }
}
