package ${packageName}.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * ${entityComment}
 */
@Data
@TableName(value = "${tableName}")
public class ${eentityName} extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;
    <#list fieldList as field>
	<#if (field.camelName) != 'insertTime' && (field.camelName) != 'updateTime' && (field.camelName) != 'insertUserId' && (field.camelName) != 'operateUserId'>

    private ${field.javaType} ${field.camelName};    // ${field.comment}
	<#if (field.dictionaryType)??>
    
    @TableField(exist = false)
    private String ${field.camelNameDic};    // ${field.commentDic}（数据字典值）
	</#if>
	</#if>
    </#list>
	<#list toFromList as toFrom>

    private Long ${toFrom.toFromEntityName}Id;    // ${toFrom.toFromComment}ID
	</#list>

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    <#list fieldList as field>
	<#if (field.camelName) != 'insertTime' && (field.camelName) != 'updateTime' && (field.camelName) != 'insertUserId' && (field.camelName) != 'operateUserId'>

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
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (${eentityName}.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ${eentityName} ${entityName} = (${eentityName}) o;
        if (${entityName}.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ${entityName}.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "${eentityName}{" +
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
