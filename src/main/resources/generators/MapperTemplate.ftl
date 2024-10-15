package ${basePackage}.${entityName?lower_case};

import ${basePackage}.core.entity.BaseMapper;
import org.mapstruct.*;
import ${basePackage}.${entityName?lower_case}.dto.*;
<#assign imports = []>
<#list relationships as rel>
    <#if !imports?seq_contains("${rel.relatedEntityPackage}.${rel.relatedEntityName}Mapper")>
        <#assign imports += ["${rel.relatedEntityPackage}.${rel.relatedEntityName}Mapper"]>
    </#if>
    <#if !imports?seq_contains("${rel.relatedEntityPackage}.dto.${rel.relatedEntityName}LoadDto")>
        <#assign imports += ["${rel.relatedEntityPackage}.dto.${rel.relatedEntityName}LoadDto"]>
    </#if>
    <#if !imports?seq_contains("${rel.relatedEntityPackage}.${rel.relatedEntityName}Entity")>
        <#assign imports += ["${rel.relatedEntityPackage}.${rel.relatedEntityName}Entity"]>
    </#if>
    <#if rel.type.type == "OneToMany" || rel.type.type == "ManyToMany">
        <#if !imports?seq_contains("java.util.List")>
            <#assign imports += ["java.util.List"]>
        </#if>
    </#if>
    <#if !imports?seq_contains("org.mapstruct.factory.Mappers")>
        <#assign imports += ["org.mapstruct.factory.Mappers"]>
    </#if>
    <#if rel.type.type == "OneToMany" || rel.type.type == "ManyToMany">
        <#if !imports?seq_contains("java.util.stream.Collectors")>
            <#assign imports += ["java.util.stream.Collectors"]>
        </#if>
    </#if>
</#list>
<#list imports as imp>
import ${imp};
</#list>

@Mapper(componentModel = "spring", uses = {
<#list relationships as rel>
    ${rel.relatedEntityName}Mapper.class<#if rel_has_next>,</#if>
</#list>
})
public interface ${entityName}Mapper extends BaseMapper<${entityName}Entity, ${entityName}CreateDto, ${entityName}UpdateDto, ${entityName}LoadDto> {
<#-- Mapping for List relations -->
<#list relationships as rel>
    <#if rel.type.type == "OneToOne" || rel.type.type == "ManyToOne">
    @Named("${rel.relatedEntityName}To${rel.relatedEntityName}Dto")
    default ${rel.relatedEntityName}LoadDto ${rel.relatedEntityName?uncap_first}To${rel.relatedEntityName}Dto(${rel.relatedEntityName}Entity ${rel.relatedEntityName?uncap_first}) {
        return Mappers.getMapper(${rel.relatedEntityName}Mapper.class).load(${rel.relatedEntityName?uncap_first});
    }
    </#if>
    <#if rel.type.type == "OneToMany" || rel.type.type == "ManyToMany">
    @Named("${rel.relatedEntityName}ListToDto")
    default List<${rel.relatedEntityName}LoadDto> ${rel.relatedEntityName?uncap_first}ListTo${rel.relatedEntityName}DtoList(List<${rel.relatedEntityName}Entity> ${rel.relatedEntityName?uncap_first}) {
        return ${rel.relatedEntityName?uncap_first}.stream()
            .map(e -> Mappers.getMapper(${rel.relatedEntityName}Mapper.class).load(e))
            .collect(Collectors.toList());
    }
    </#if>
</#list>
}
