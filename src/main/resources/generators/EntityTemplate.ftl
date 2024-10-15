package ${basePackage}.${entityName?lower_case};

import ${basePackage}.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
<#assign imports = []>
<#assign entityImports = []>
<#list fields as field>
    <#if field.type.requiresImport()>
        <#if !imports?seq_contains(field.type.importPath)>
            <#assign imports += [field.type.importPath]>
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
    <#if rel.relatedEntityName != entityName>
        <#if !entityImports?seq_contains("${rel.relatedEntityPackage}.${rel.relatedEntityName}Entity")>
            <#assign entityImports += ["${rel.relatedEntityPackage}.${rel.relatedEntityName}Entity"]>
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
    ${field.type.type} ${field.name};
</#list>

<#list relationships as rel>
    <#if rel.type.type == "OneToOne">
        <#if rel.hasMappedBy()>
    @OneToOne(mappedBy = "${rel.mappedBy}", cascade = CascadeType.ALL)
        <#else>
    @OneToOne(cascade = CascadeType.ALL)
        </#if>
    private ${rel.relatedEntityName}Entity ${rel.relatedEntityName?uncap_first};
    </#if>
    <#if rel.type.type == "OneToMany">
        <#if rel.hasMappedBy()>
    @OneToMany(mappedBy = "${rel.mappedBy}", cascade = CascadeType.ALL)
        <#else>
    @OneToMany(cascade = CascadeType.ALL)
        </#if>
    List<${rel.relatedEntityName}Entity> ${rel.relatedEntityName?uncap_first};
    </#if>
    <#if rel.type.type == "ManyToOne">
    @ManyToOne
        <#if rel.required>
    @JoinColumn(name = "${rel.foreignKey}", nullable = false)
        <#else>
    @JoinColumn(name = "${rel.foreignKey}")
        </#if>
    ${rel.relatedEntityName}Entity ${rel.relatedEntityName?uncap_first};
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