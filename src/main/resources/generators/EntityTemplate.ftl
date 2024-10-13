package ${basePackage}.${entityName?lower_case};

import com.alishahidi.api.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
<#assign imports = []>
<#assign entityImports = []>
<#list relationships as rel>
    <#if rel.relationshipType == "OneToMany" || rel.relationshipType == "ManyToMany">
        <#if !imports?seq_contains("java.util.List")>
            <#assign imports += ["java.util.List"]>
        </#if>
    </#if>
    <#if rel.relationshipType == "ManyToMany">
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
    ${field.type} ${field.name};
</#list>

<#list relationships as rel>
    <#if rel.relationshipType == "OneToOne">
    @OneToOne(mappedBy = "${rel.mappedBy}", cascade = CascadeType.ALL)
    private ${rel.relatedEntityName}Entity ${rel.relatedEntityName?uncap_first};
    </#if>
    <#if rel.relationshipType == "OneToMany">
    @OneToMany(mappedBy = "${rel.mappedBy}", cascade = CascadeType.ALL)
    private List<${rel.relatedEntityName}Entity> ${rel.relatedEntityName?uncap_first}s;
    </#if>
    <#if rel.relationshipType == "ManyToOne">
    @ManyToOne
    @JoinColumn(name = "${rel.foreignKey}", nullable = false)
    private ${rel.relatedEntityName}Entity ${rel.relatedEntityName?uncap_first};
    </#if>
    <#if rel.relationshipType == "ManyToMany">
    @ManyToMany
    @JoinTable(
    name = "${entityName?lower_case}_${rel.relatedEntityName?lower_case}",
    joinColumns = @JoinColumn(name = "${entityName?lower_case}_id"),
    inverseJoinColumns = @JoinColumn(name = "${rel.relatedEntityName?lower_case}_id")
    )
    private Set<${rel.relatedEntityName}Entity> ${rel.relatedEntityName?uncap_first}s;
    </#if>
</#list>
}