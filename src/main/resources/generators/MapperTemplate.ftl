package ${basePackage}.${entityName?lower_case};

import ${basePackage}.core.entity.BaseMapper;
import org.mapstruct.*;
import ${basePackage}.${entityName?lower_case}.dto.*;
<#assign imports = []>
<#assign documentIncluded = false>
<#list relationships as rel>
    <#if rel.document>
        <#if !imports?seq_contains("${basePackage}.core.document.DocumentMapper")>
            <#assign imports += ["${basePackage}.core.document.DocumentMapper"]>
        </#if>
        <#if !imports?seq_contains("${basePackage}.core.document.DocumentDto")>
            <#assign imports += ["${basePackage}.core.document.DocumentDto"]>
        </#if>
        <#if !imports?seq_contains("${basePackage}.core.document.Document")>
            <#assign imports += ["${basePackage}.core.document.Document"]>
        </#if>
    <#else>
        <#if !imports?seq_contains("${rel.relatedEntityPackage}.${rel.relatedEntityName}Mapper")>
            <#assign imports += ["${rel.relatedEntityPackage}.${rel.relatedEntityName}Mapper"]>
        </#if>
        <#if !imports?seq_contains("${rel.relatedEntityPackage}.dto.${rel.relatedEntityName}LoadDto")>
            <#assign imports += ["${rel.relatedEntityPackage}.dto.${rel.relatedEntityName}LoadDto"]>
        </#if>
        <#if !imports?seq_contains("${rel.relatedEntityPackage}.${rel.relatedEntityName}Entity")>
            <#assign imports += ["${rel.relatedEntityPackage}.${rel.relatedEntityName}Entity"]>
        </#if>
    </#if>
    <#if !imports?seq_contains("org.mapstruct.factory.Mappers")>
        <#assign imports += ["org.mapstruct.factory.Mappers"]>
    </#if>
</#list>
<#list imports as imp>
import ${imp};
</#list>

@Mapper(componentModel = "spring")
public interface ${entityName}Mapper extends BaseMapper<${entityName}Entity, ${entityName}CreateDto, ${entityName}UpdateDto, ${entityName}LoadDto> {
<#-- Mapping for List relations -->
<#list relationships as rel>
<#if rel.mappedBy?has_content>

    <#if rel.document>
        <#if documentIncluded == false>
    default DocumentDto DocumentToDocumentDto(Document document) {
        return Mappers.getMapper(DocumentMapper.class).toDto(document);
    }
            <#assign documentIncluded = true>
        </#if>
    <#else>
    default ${rel.relatedEntityName}LoadDto ${rel.relatedEntityName?uncap_first}To${rel.relatedEntityName}Dto(${rel.relatedEntityName}Entity ${rel.relatedEntityName?uncap_first}) {
        return Mappers.getMapper(${rel.relatedEntityName}Mapper.class).load(${rel.relatedEntityName?uncap_first});
    }
    </#if>
</#if>
</#list>
}
