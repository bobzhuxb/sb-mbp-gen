package ${packageName}.dto.criteria;

import ${packageName}.dto.criteria.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * ${entityComment} 条件过滤器
 */
public class ${eentityName}Criteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;
    <#list fieldList as field>

    private <#if field.javaType == 'String'>StringFilter<#elseif field.javaType == 'Long'>LongFilter<#elseif field.javaType == 'Integer'>IntegerFilter<#elseif field.javaType == 'Double'>DoubleFilter<#else>Filter</#if> ${field.camelName};    // ${field.comment}
    </#list>
	<#list toFromList as toFrom>

    private LongFilter ${toFrom.toFromEntityName}Id;    // ${toFrom.toFromComment}ID
	</#list>
	<#list fromToList as fromTo>

    private LongFilter ${fromTo.fromToEntityName}Id;    // ${fromTo.fromToComment}ID
	</#list>
	<#list toFromList as toFrom>

    private ${toFrom.toFromEntityType}Criteria ${toFrom.toFromEntityName};    // ${toFrom.toFromComment}
	</#list>

    public ${eentityName}Criteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    <#list fieldList as field>
    
    public <#if field.javaType == 'String'>StringFilter<#elseif field.javaType == 'Long'>LongFilter<#elseif field.javaType == 'Integer'>IntegerFilter<#elseif field.javaType == 'Double'>DoubleFilter<#else>Filter</#if> get${field.ccamelName}() {
        return ${field.camelName};
    }

    public void set${field.ccamelName}(<#if field.javaType == 'String'>StringFilter<#elseif field.javaType == 'Long'>LongFilter<#elseif field.javaType == 'Integer'>IntegerFilter<#elseif field.javaType == 'Double'>DoubleFilter<#else>Filter</#if> ${field.camelName}) {
        this.${field.camelName} = ${field.camelName};
    }
	</#list>
	<#list toFromList as toFrom>

    public LongFilter get${toFrom.toFromEntityType}Id() {
        return ${toFrom.toFromEntityName}Id;
    }
	
	public void set${toFrom.toFromEntityType}Id(LongFilter ${toFrom.toFromEntityName}Id) {
        this.${toFrom.toFromEntityName}Id = ${toFrom.toFromEntityName}Id;
    }
	</#list>
	<#list fromToList as fromTo>

    public LongFilter get${fromTo.fromToEntityType}Id() {
        return ${fromTo.fromToEntityName}Id;
    }
	
	public void set${fromTo.fromToEntityType}Id(LongFilter ${fromTo.fromToEntityName}Id) {
        this.${fromTo.fromToEntityName}Id = ${fromTo.fromToEntityName}Id;
    }
	</#list>
	<#list toFromList as toFrom>

    public ${toFrom.toFromEntityType}Criteria get${toFrom.toFromEntityType}() {
        return ${toFrom.toFromEntityName};
    }

    public void set${toFrom.toFromEntityType}(${toFrom.toFromEntityType}Criteria ${toFrom.toFromEntityName}) {
        this.${toFrom.toFromEntityName} = ${toFrom.toFromEntityName};
    }
	</#list>

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ${eentityName}Criteria that = (${eentityName}Criteria) o;
        return
            Objects.equals(id, that.id) &&
			<#list fieldList as field>
            Objects.equals(${field.camelName}, that.${field.camelName})<#if field_has_next> &&<#else>;</#if>
			</#list>
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
		<#list fieldList as field>
        ${field.camelName}<#if field_has_next>,</#if>
		</#list>
        );
    }

    @Override
    public String toString() {
        return "${eentityName}Criteria{" +
                (id != null ? "id=" + id + ", " : "") +
				<#list fieldList as field>
                (${field.camelName} != null ? "${field.camelName}=" + ${field.camelName} + ", " : "") +
				</#list>
            "}";
    }

}
