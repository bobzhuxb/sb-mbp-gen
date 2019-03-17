package ${packageName}.config;

import ${packageName}.dto.help.BaseEntityConfigDTO;
import ${packageName}.dto.help.BaseEntityConfigDicDTO;
import ${packageName}.service.*;
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
}