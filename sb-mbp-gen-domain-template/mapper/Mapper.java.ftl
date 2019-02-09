package ${packageName}.mapper;

import ${packageName}.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * ${entityComment} Mapper
 */
public interface ${eentityName}Mapper extends BaseCommonMapper<${eentityName}> {
	<#list toFromList as toFrom>

    // 级联置空${toFrom.fromColumnName}字段
    void ${toFrom.toFromEntityName}IdCascadeToNull(@Param("${toFrom.toFromEntityName}Id")long ${toFrom.toFromEntityName}Id);
	</#list>

}
