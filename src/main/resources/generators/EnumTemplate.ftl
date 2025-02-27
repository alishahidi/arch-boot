package ${basePackage}.${entityName?lower_case}.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ${entityName?cap_first}${enumName?cap_first}Enum {
    <#list enums as enum>
    ${enum.name?upper_case}("${enum.value}")<#if enum_has_next>,<#else>;</#if>
    </#list>

    private final String name;

    public static ${entityName?cap_first}${enumName?cap_first}Enum fromFieldName(String name) {
        for (${entityName?cap_first}${enumName?cap_first}Enum field : ${entityName?cap_first}${enumName?cap_first}Enum.values()) {
            if (field.getName().equalsIgnoreCase(name)) {
                return field;
            }
        }
        throw new IllegalArgumentException("No enum constant with field name " + name);
    }
}
