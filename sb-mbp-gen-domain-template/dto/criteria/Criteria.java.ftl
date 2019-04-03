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
	<#if (field.camelName) != 'insertTime' && (field.camelName) != 'updateTime' && (field.camelName) != 'insertUserId' && (field.camelName) != 'operateUserId'>

    private <#if field.javaType == 'String'>StringFilter<#elseif field.javaType == 'Long'>LongFilter<#elseif field.javaType == 'Integer'>IntegerFilter<#elseif field.javaType == 'Double'>DoubleFilter<#else>Filter</#if> ${field.camelName};    // ${field.comment}
	</#if>
    </#list>
	<#list toFromList as toFrom>

    private LongFilter ${toFrom.toFromEntityName}Id;    // ${toFrom.toFromComment}ID
	</#list>
	<#list toFromList as toFrom>

    private ${toFrom.toFromEntityType}Criteria ${toFrom.toFromEntityName};    // ${toFrom.toFromComment}
	</#list>
	<#list fromToList as fromTo>

    private ${fromTo.fromToEntityType}Criteria ${fromTo.fromToEntityName}List;    // ${fromTo.fromToComment}
	</#list>

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public ${eentityName}Criteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    <#list fieldList as field>
	<#if (field.camelName) != 'insertTime' && (field.camelName) != 'updateTime' && (field.camelName) != 'insertUserId' && (field.camelName) != 'operateUserId'>
    
    public <#if field.javaType == 'String'>StringFilter<#elseif field.javaType == 'Long'>LongFilter<#elseif field.javaType == 'Integer'>IntegerFilter<#elseif field.javaType == 'Double'>DoubleFilter<#else>Filter</#if> get${field.ccamelName}() {
        return ${field.camelName};
    }

    public void set${field.ccamelName}(<#if field.javaType == 'String'>StringFilter<#elseif field.javaType == 'Long'>LongFilter<#elseif field.javaType == 'Integer'>IntegerFilter<#elseif field.javaType == 'Double'>DoubleFilter<#else>Filter</#if> ${field.camelName}) {
        this.${field.camelName} = ${field.camelName};
    }
	</#if>
	</#list>
	<#list toFromList as toFrom>

    public LongFilter get${toFrom.toFromEntityUName}Id() {
        return ${toFrom.toFromEntityName}Id;
    }
	
	public void set${toFrom.toFromEntityUName}Id(LongFilter ${toFrom.toFromEntityName}Id) {
        this.${toFrom.toFromEntityName}Id = ${toFrom.toFromEntityName}Id;
    }
	</#list>
	<#list toFromList as toFrom>

    public ${toFrom.toFromEntityType}Criteria get${toFrom.toFromEntityUName}() {
        return ${toFrom.toFromEntityName};
    }

    public void set${toFrom.toFromEntityUName}(${toFrom.toFromEntityType}Criteria ${toFrom.toFromEntityName}) {
        this.${toFrom.toFromEntityName} = ${toFrom.toFromEntityName};
    }
	</#list>
	<#list fromToList as fromTo>

    public ${fromTo.fromToEntityType}Criteria get${fromTo.fromToEntityUName}List() {
        return ${fromTo.fromToEntityName}List;
    }

    public void set${fromTo.fromToEntityUName}List(${fromTo.fromToEntityType}Criteria ${fromTo.fromToEntityName}List) {
        this.${fromTo.fromToEntityName}List = ${fromTo.fromToEntityName}List;
    }
	</#list>

    // ================self code:增强的查询条件get/set方法start=====================
    // ================self code:增强的查询条件get/set方法end=====================

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ${eentityName}Criteria that = (${eentityName}Criteria) o;
        return Objects.equals(id, that.id)
				<#list fieldList as field>
				<#if (field.camelName) != 'insertTime' && (field.camelName) != 'updateTime' && (field.camelName) != 'insertUserId' && (field.camelName) != 'operateUserId'>
                && Objects.equals(${field.camelName}, that.${field.camelName})
				</#if>
				</#list>
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id
				<#list fieldList as field>
				<#if (field.camelName) != 'insertTime' && (field.camelName) != 'updateTime' && (field.camelName) != 'insertUserId' && (field.camelName) != 'operateUserId'>
                , ${field.camelName}
				</#if>
				</#list>
        );
    }

    @Override
    public String toString() {
        return "${eentityName}Criteria{" +
                (id != null ? "id=" + id + ", " : "") +
				<#list fieldList as field>
				<#if (field.camelName) != 'insertTime' && (field.camelName) != 'updateTime' && (field.camelName) != 'insertUserId' && (field.camelName) != 'operateUserId'>
                (${field.camelName} != null ? "${field.camelName}=" + ${field.camelName} + ", " : "") +
				</#if>
				</#list>
                "}";
    }

}
