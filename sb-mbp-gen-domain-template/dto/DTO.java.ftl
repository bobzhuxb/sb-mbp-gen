package ${packageName}.dto;

<#if fromToList?? && (fromToList?size > 0) >
import java.util.List;
</#if>
<#list fromToList as fromTo>
import ${packageName}.domain.${fromTo.fromToEntityType};
</#list>
import java.util.Objects;

/**
 * ${entityComment}
 */
public class ${eentityName}DTO extends BaseDTO {

    private Long id;
	<#list fieldList as field>

    private ${field.javaType} ${field.camelName};    // ${field.comment}
	</#list>
	<#list toFromList as toFrom>

    private Long ${toFrom.toFromEntityName}Id;    // ${toFrom.toFromComment}ID
	</#list>
	<#if (toFromList?? && (toFromList?size > 0)) || (fromToList?? && (fromToList?size > 0)) >
    
	///////////////////////// 附加关联属性 /////////////////////////
	</#if>
	<#list toFromList as toFrom>

    private ${toFrom.toFromEntityType}DTO ${toFrom.toFromEntityName};    // ${toFrom.toFromComment}
	</#list>
	<#list fromToList as fromTo>

	<#if fromTo.relationType == "OneToMany">
    private List<${fromTo.fromToEntityType}DTO> ${fromTo.fromToEntityName}List;    // ${fromTo.fromToComment}列表
	</#if>
	<#if fromTo.relationType == "OneToOne">
    private ${fromTo.fromToEntityType}DTO ${fromTo.fromToEntityName};    // ${fromTo.fromToComment}
	</#if>
	</#list>

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	<#list fieldList as field>
	
    public ${field.javaType} get${field.ccamelName}() {
        return ${field.camelName};
    }

    public void set${field.ccamelName}(${field.javaType} ${field.camelName}) {
        this.${field.camelName} = ${field.camelName};
    }
	</#list>
	<#list toFromList as toFrom>

    public Long get${toFrom.toFromEntityType}Id() {
        return ${toFrom.toFromEntityName}Id;
    }

    public void set${toFrom.toFromEntityType}Id(Long ${toFrom.toFromEntityName}Id) {
        this.${toFrom.toFromEntityName}Id = ${toFrom.toFromEntityName}Id;
    }
	</#list>
	<#list toFromList as toFrom>

    public ${toFrom.toFromEntityType}DTO get${toFrom.toFromEntityType}() {
        return ${toFrom.toFromEntityName};
    }

    public void set${toFrom.toFromEntityType}(${toFrom.toFromEntityType}DTO ${toFrom.toFromEntityName}) {
        this.${toFrom.toFromEntityName} = ${toFrom.toFromEntityName};
    }
	</#list>
	<#list fromToList as fromTo>

	<#if fromTo.relationType == "OneToMany">
    public List<${fromTo.fromToEntityType}DTO> get${fromTo.fromToEntityType}List() {
        return ${fromTo.fromToEntityName}List;
    }

    public void set${fromTo.fromToEntityType}List(List<${fromTo.fromToEntityType}DTO> ${fromTo.fromToEntityName}List) {
        this.${fromTo.fromToEntityName}List = ${fromTo.fromToEntityName}List;
    }
	</#if>
	<#if fromTo.relationType == "OneToOne">
    public ${fromTo.fromToEntityType}DTO get${fromTo.fromToEntityType}() {
        return ${fromTo.fromToEntityName};
    }

    public void set${fromTo.fromToEntityType}(${fromTo.fromToEntityType}DTO ${fromTo.fromToEntityName}) {
        this.${fromTo.fromToEntityName} = ${fromTo.fromToEntityName};
    }
	</#if>
	</#list>

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ${eentityName}DTO ${entityName}DTO = (${eentityName}DTO) o;
        if (${entityName}DTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ${entityName}DTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "${eentityName}DTO{" +
            "id=" + getId() +
            <#list fieldList as field>
            ", ${field.camelName}=<#if field.javaType == 'String'>'</#if>" + get${field.ccamelName}()<#if field.javaType == 'String'> + "'"</#if> +
            </#list>
			<#list toFromList as toFrom>
			", ${toFrom.toFromEntityName}Id=" + get${toFrom.toFromEntityType}Id() +
			</#list>
            "}";
    }
}
