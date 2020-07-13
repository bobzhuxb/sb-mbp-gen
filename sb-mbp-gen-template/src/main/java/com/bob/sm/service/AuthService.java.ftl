package ${packageName}.service;

import ${packageName}.dto.criteria.BaseCriteria;
import ${packageName}.dto.help.InnerAuthFilterOperateDTO;
import ${packageName}.dto.help.InnerUserInfoDetailDTO;
import ${packageName}.dto.help.ReturnCommonDTO;

import java.util.List;
import java.util.Map;

/**
 * 数据权限相关
 * @author Bob
 */
public interface AuthService {

    InnerUserInfoDetailDTO commonGetCurrentUser();

    boolean commonDataAuthorityFilter(BaseCriteria baseCriteria, Map<String, Object> appendParamMap,
                                      ReturnCommonDTO interceptReturnInfo, String organizationIdFilterStr,
                                      List<InnerAuthFilterOperateDTO> operateList);

}
