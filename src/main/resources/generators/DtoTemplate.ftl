package ${basePackage}.${entityName?lower_case}.dto;

import ${basePackage}.core.entity.BaseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
<#assign imports = []>
<#list fields as field>
    <#if field.type.type == "Enum">
        <#if !imports?seq_contains("${basePackage}.${entityName?lower_case}.constant.${entityName?cap_first}${field.name?cap_first}Enum")>
            <#assign imports += ["${basePackage}.${entityName?lower_case}.constant.${entityName?cap_first}${field.name?cap_first}Enum"]>
        </#if>
    <#else>
        <#if field.type.requiresImport()>
            <#if !imports?seq_contains(field.type.importPath)>
                <#assign imports += [field.type.importPath]>
            </#if>
        </#if>
    </#if>
</#list>
<#list relationships as rel>
    <#if rel.document>
        <#assign imports += [basePackage + ".core.document.DocumentDto"]>
    <#else>
        <#if !imports?seq_contains("${rel.relatedEntityPackage}.dto.${rel.relatedEntityName}LoadDto")>
            <#assign imports += ["${rel.relatedEntityPackage}.dto.${rel.relatedEntityName}LoadDto"]>
        </#if>
    </#if>
    <#if rel.type.type == "OneToMany" || rel.type.type == "ManyToMany">
        <#if !imports?seq_contains("java.util.List")>
            <#assign imports += ["java.util.List"]>
        </#if>
    </#if>
</#list>
<#list imports as imp>
import ${imp};
</#list>

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ${entityName}${dtoType}Dto extends BaseDto {
<#list fields as field>
    <#if field.type.type == "Enum">
    ${entityName?cap_first}${field.name?cap_first}Enum ${field.name};
    <#else>
    ${field.type.type} ${field.name};
    </#if>
</#list>
<#list relationships as rel>
<#if rel.mappedBy?has_content>
    <#if rel.type.type == "OneToOne" || rel.type.type == "ManyToOne">
        <#if rel.document>
    DocumentDto ${rel.name?uncap_first};
        <#else>
    ${rel.relatedEntityName}LoadDto ${rel.relatedEntityName?uncap_first};
        </#if>
    </#if>
    <#if rel.type.type == "OneToMany" || rel.type.type == "ManyToMany">
        <#if rel.document>
    List<DocumentDto> ${rel.name?uncap_first};
        <#else>
    List<${rel.relatedEntityName}LoadDto> ${rel.relatedEntityName?uncap_first};
        </#if>
    </#if>
</#if>
</#list>
}
