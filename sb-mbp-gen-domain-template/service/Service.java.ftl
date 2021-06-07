package ${packageName}.service;

import ${packageName}.domain.${eentityName};
import ${packageName}.dto.${eentityName}DTO;
import ${packageName}.dto.criteria.${eentityName}Criteria;
<#if eentityName == 'SystemPermission'>
import ${packageName}.dto.help.PageElementDTO;
import ${packageName}.dto.help.RolePageElementsDTO;
</#if>

import java.util.List;
import java.util.Map;

/**
 * ${entityComment}
 * @author Bob
 */
public interface ${eentityName}Service extends BaseService<${eentityName}, ${eentityName}Criteria, ${eentityName}DTO> {

}
