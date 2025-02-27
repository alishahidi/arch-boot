package ${basePackage}.${entityName?lower_case};

import ${basePackage}.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
<#assign imports = []>
<#assign entityImports = []>
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
    <#if rel.type.type == "OneToMany" || rel.type.type == "ManyToMany">
        <#if !imports?seq_contains("java.util.List")>
            <#assign imports += ["java.util.List"]>
        </#if>
    </#if>
    <#if rel.type.type == "ManyToMany">
        <#if !imports?seq_contains("java.util.Set")>
            <#assign imports += ["java.util.Set"]>
        </#if>
    </#if>
    <#if rel.document>
        <#if !imports?seq_contains(basePackage + ".core.document.Document")>
            <#assign imports += [basePackage + ".core.document.Document"]>
        </#if>
    <#else>
        <#if rel.relatedEntityName != entityName>
            <#if !entityImports?seq_contains("${rel.relatedEntityPackage}.${rel.relatedEntityName}Entity")>
                <#assign entityImports += ["${rel.relatedEntityPackage}.${rel.relatedEntityName}Entity"]>
            </#if>
        </#if>
    </#if>
</#list>
<#list imports as imp>
import ${imp};
</#list>
<#list entityImports as imp>
import ${imp};
</#list>

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "${tableName}")
public class ${entityName}Entity extends BaseEntity {

<#list fields as field>
    <#if field.required>
    @Column(nullable = false)
    </#if>
    <#if field.type.type == "Enum">
    @Enumerated(EnumType.STRING)
    ${entityName?cap_first}${field.name?cap_first}Enum ${field.name};
    <#else>
    ${field.type.type} ${field.name};
    </#if>
</#list>

<#list relationships as rel>
    <#if rel.type.type == "OneToOne">
        <#if rel.hasMappedBy()>
    @OneToOne(mappedBy = "${rel.mappedBy}", cascade = CascadeType.ALL)
        <#else>
    @OneToOne(cascade = CascadeType.ALL)
        </#if>
        <#if rel.document>
    Document ${rel.name?uncap_first};
        <#else>
    ${rel.relatedEntityName}Entity ${rel.relatedEntityName?uncap_first};
        </#if>
    </#if>
    <#if rel.type.type == "OneToMany">
        <#if rel.hasMappedBy()>
    @OneToMany(mappedBy = "${rel.mappedBy}", cascade = CascadeType.ALL)
        <#else>
    @OneToMany(cascade = CascadeType.ALL)
        </#if>
        <#if rel.document>
    List<Document> ${rel.name?uncap_first};
        <#else>
    List<${rel.relatedEntityName}Entity> ${rel.relatedEntityName?uncap_first};
        </#if>
    </#if>
    <#if rel.type.type == "ManyToOne">
    @ManyToOne
        <#if rel.required>
    @JoinColumn(name = "${rel.relatedEntityName?lower_case}_id", nullable = false)
        <#else>
    @JoinColumn(name = "${rel.relatedEntityName?lower_case}_id")
        </#if>
    <#if rel.document>
        Document ${rel.name?uncap_first};
    <#else>
        ${rel.relatedEntityName}Entity ${rel.relatedEntityName?uncap_first};
    </#if>
    </#if>
    <#if rel.type.type == "ManyToMany">
        <#if rel.hasMappedBy()>
    @ManyToMany(mappedBy = "${rel.mappedBy}")
        <#else>
    @ManyToMany
        </#if>
    @JoinTable(
        name = "${entityName?lower_case}_${rel.relatedEntityName?lower_case}",
        joinColumns = @JoinColumn(name = "${entityName?lower_case}_id"),
        inverseJoinColumns = @JoinColumn(name = "${rel.relatedEntityName?lower_case}_id")
    )
    Set<${rel.relatedEntityName}Entity> ${rel.relatedEntityName?uncap_first};
    </#if>
</#list>
}