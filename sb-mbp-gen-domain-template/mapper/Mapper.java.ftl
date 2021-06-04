package ${packageName}.mapper;

import ${packageName}.domain.*;
import org.apache.ibatis.annotations.Param;

<#if eentityName == 'SystemPermission'>

import java.util.List;
</#if>
/**
 * ${entityComment} Mapper
 * @author Bob
 */
public interface ${eentityName}Mapper extends BaseCommonMapper<${eentityName}> {

	<#if eentityName == 'SystemPermission'>
    List<String> findPermissionIdentifyByLogin(@Param("login")String login);

	</#if>
}
