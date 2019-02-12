package ${packageName}.dto;

import ${packageName}.annotation.*;
import ${packageName}.annotation.validation.*;
import ${packageName}.domain.*;
import javax.validation.constraints.*;
<#if fromToList?? && (fromToList?size > 0) >
import java.util.List;
</#if>
import java.util.Objects;

/**
 * ${entityComment}
 */
<#if (annotationList)??>
<#list annotationList as annotation>
${annotation}
</#list>
</#if>
public class ${eentityName}DTO extends BaseDTO {

    private Long id;
	<#list fieldList as field>
	<#if field.camelName != 'insertTime' && field.camelName != 'updateTime' && field.camelName != 'operateUserId'>

	<#if (field.annotationList)??>
	<#list field.annotationList as annotation>
    ${annotation}
	</#list>
	</#if>
    private ${field.javaType} ${field.camelName};    // ${field.comment}
	<#if (field.dictionaryType)??>

    private String ${field.camelNameDic};    // ${field.commentDic}（数据字典值）
	</#if>
	</#if>
	</#list>
	<#list toFromList as toFrom>

	<#if (toFrom.annotationList)??>
	<#list toFrom.annotationList as annotation>
    ${annotation}
	</#list>
	</#if>
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

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	<#list fieldList as field>
	<#if field.camelName != 'insertTime' && field.camelName != 'updateTime' && field.camelName != 'operateUserId'>
	
    public ${field.javaType} get${field.ccamelName}() {
        return ${field.camelName};
    }

    public void set${field.ccamelName}(${field.javaType} ${field.camelName}) {
        this.${field.camelName} = ${field.camelName};
    }
	<#if (field.dictionaryType)??>
    
    public String get${field.ccamelNameDic}() {
        return ${field.camelNameDic};
    }

    public void set${field.ccamelNameDic}(String ${field.camelNameDic}) {
        this.${field.camelNameDic} = ${field.camelNameDic};
    }
	</#if>
	</#if>
	</#list>
	<#list toFromList as toFrom>

    public Long get${toFrom.toFromEntityUName}Id() {
        return ${toFrom.toFromEntityName}Id;
    }

    public void set${toFrom.toFromEntityUName}Id(Long ${toFrom.toFromEntityName}Id) {
        this.${toFrom.toFromEntityName}Id = ${toFrom.toFromEntityName}Id;
    }
	</#list>
	<#list toFromList as toFrom>

    public ${toFrom.toFromEntityType}DTO get${toFrom.toFromEntityUName}() {
        return ${toFrom.toFromEntityName};
    }

    public void set${toFrom.toFromEntityUName}(${toFrom.toFromEntityType}DTO ${toFrom.toFromEntityName}) {
        this.${toFrom.toFromEntityName} = ${toFrom.toFromEntityName};
    }
	</#list>
	<#list fromToList as fromTo>

	<#if fromTo.relationType == "OneToMany">
    public List<${fromTo.fromToEntityType}DTO> get${fromTo.fromToEntityUName}List() {
        return ${fromTo.fromToEntityName}List;
    }

    public void set${fromTo.fromToEntityUName}List(List<${fromTo.fromToEntityType}DTO> ${fromTo.fromToEntityName}List) {
        this.${fromTo.fromToEntityName}List = ${fromTo.fromToEntityName}List;
    }
	</#if>
	<#if fromTo.relationType == "OneToOne">
    public ${fromTo.fromToEntityType}DTO get${fromTo.fromToEntityUName}() {
        return ${fromTo.fromToEntityName};
    }

    public void set${fromTo.fromToEntityUName}(${fromTo.fromToEntityType}DTO ${fromTo.fromToEntityName}) {
        this.${fromTo.fromToEntityName} = ${fromTo.fromToEntityName};
    }
	</#if>
	</#list>

    // ================self code:自定义属性的get/set方法start=====================
    // ================self code:自定义属性的get/set方法end=====================

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
			", ${toFrom.toFromEntityName}Id=" + get${toFrom.toFromEntityUName}Id() +
			</#list>
            "}";
    }
}
