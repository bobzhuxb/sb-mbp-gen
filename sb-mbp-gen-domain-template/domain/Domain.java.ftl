package ${packageName}.domain;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Objects;

/**
 * ${entityComment}
 * @author Bob
 */
public class ${eentityName} extends BaseDomain {

    private static final long serialVersionUID = 1L;
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

    private String ${toFrom.toFromEntityName}Id;    // ${toFrom.toFromComment}ID
	</#list>
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

    public String get${toFrom.toFromEntityUName}Id() {
        return ${toFrom.toFromEntityName}Id;
    }

    public void set${toFrom.toFromEntityUName}Id(String ${toFrom.toFromEntityName}Id) {
        this.${toFrom.toFromEntityName}Id = ${toFrom.toFromEntityName}Id;
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

    // 数据表名和列名
    public static final String _TableName = "${tableName}";
    <#list fieldList as field>
	<#if (field.camelName) != 'insertTime' && (field.camelName) != 'updateTime' && (field.camelName) != 'insertUserId' && (field.camelName) != 'operateUserId'>
    public static final String _${field.camelName} = "${field.camelNameUnderline}";    // ${field.comment}
	<#if (field.dictionaryType)??>
    public static final String _${field.camelNameDic} = "${field.ccamelNameDicUnderline}";    // ${field.commentDic}（数据字典）
	</#if>
	</#if>
	</#list>
	<#list toFromList as toFrom>
    public static final String _${toFrom.toFromEntityName}Id = "${toFrom.fromColumnName}";    // ${toFrom.toFromComment}ID
	</#list>

}
