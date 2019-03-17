package com.bob.sm.service.impl;

import com.bob.sm.config.GlobalCache;
import com.bob.sm.domain.BaseDomain;
import com.bob.sm.dto.help.BaseEntityConfigDTO;
import com.bob.sm.service.BaseEntityConfigService;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class BaseEntityConfigServiceImpl implements BaseEntityConfigService {

    /**
     * 初始化实体相关配置（用于共用Service方法）
     */
    public void initEntitiesConfig() {
        for (String entityName : GlobalCache.getEntityNames()) {
            BaseEntityConfigDTO baseEntityConfigDTO = new BaseEntityConfigDTO();
            GlobalCache.getEntityConfigMap().put(entityName, baseEntityConfigDTO);
            try {
                Class domainClass = Class.forName("com.bob.sm.domain." + entityName);
                BaseDomain childDomain = (BaseDomain) domainClass.getConstructor().newInstance();
                // 设置表名
                setTableName(baseEntityConfigDTO, entityName, domainClass, childDomain);
            } catch (Exception e) {
                // 没有这个类，就不用处理，忽略异常
            }
        }
    }

    /**
     * 设置表名
     * @param baseEntityConfigDTO 配置
     * @param entityName 实体名
     * @param domainClass BaseDomain的子类的Class
     * @param childDomain BaseDomain的子类的对象
     * @throws Exception
     */
    private void setTableName(BaseEntityConfigDTO baseEntityConfigDTO, String entityName, Class domainClass,
                              BaseDomain childDomain) throws Exception {
        // 获取表名（使用apache的包可以获取包括父类的属性）
        Field tableNameField = FieldUtils.getField(domainClass, "_TableName", true);
        if (tableNameField == null) {
            throw new Exception("com.bob.sm.domain." + entityName + "没有_TableName属性");
        }
        // 设置对象的访问权限，保证对private的属性的访问
        tableNameField.setAccessible(true);
        Object tableName = tableNameField.get(childDomain);
        if (tableName == null || "".equals(tableName.toString().trim())) {
            throw new Exception("com.bob.sm.domain." + entityName + "_TableName属性值为空");
        }
        baseEntityConfigDTO.setTableName(tableName.toString());

    }

}
