package ${packageName}.config;

import ${packageName}.domain.BaseDomain;
import ${packageName}.dto.BaseDTO;
import ${packageName}.dto.criteria.BaseCriteria;
import ${packageName}.dto.help.BaseEntityConfigDTO;
import ${packageName}.dto.help.BaseEntityConfigDicDTO;
import ${packageName}.dto.help.BaseEntityConfigRelationDTO;
import ${packageName}.service.*;
import ${packageName}.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GlobalCache {

    // 只在启动的时候初始化一次
    private static Map<String, BaseEntityConfigDTO> entityConfigMap = new HashMap<>();

    private static Map<String, BaseService> serviceMap = new HashMap<>();

    private static Map<String, BaseCommonMapper> mapperMap = new HashMap<>();

    private static CommonUserService commonUserService;

    private static Map<String, Class<? extends BaseDomain>> domainClassMap = new HashMap<>();

    private static Map<String, Class<? extends BaseCriteria>> criteriaClassMap = new HashMap<>();

    private static Map<String, Class<? extends BaseDTO>> dtoClassMap = new HashMap<>();

////////////////////////////add-entity-config-here////////////////////////////

    public static List<String> getEntityNames() {
        return entityNames;
    }

    public static Map<String, BaseEntityConfigDTO> getEntityConfigMap() {
        return entityConfigMap;
    }

    public static Map<String, List<BaseEntityConfigDicDTO>> getEntityDicNameMap() {
        return entityDicNameMap;
    }

    public static Map<String, BaseService> getServiceMap() {
        return serviceMap;
    }

    public static Map<String, BaseCommonMapper> getMapperMap() {
        return mapperMap;
    }

    public static CommonUserService getCommonUserService() {
        return commonUserService;
    }

    public static Map<String, Class<? extends BaseDomain>> getDomainClassMap() {
        return domainClassMap;
    }

    public static Map<String, Class<? extends BaseCriteria>> getCriteriaClassMap() {
        return criteriaClassMap;
    }

    public static Map<String, Class<? extends BaseDTO>> getDtoClassMap() {
        return dtoClassMap;
    }

    public static Map<String, List<BaseEntityConfigRelationDTO>> getEntityRelationsMap() {
        return entityRelationsMap;
    }
}
