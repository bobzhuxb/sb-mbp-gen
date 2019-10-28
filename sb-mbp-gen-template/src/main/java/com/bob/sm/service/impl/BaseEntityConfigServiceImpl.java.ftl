package ${packageName}.service.impl;

import ${packageName}.config.GlobalCache;
import ${packageName}.domain.BaseDomain;
import ${packageName}.dto.help.BaseEntityConfigDTO;
import ${packageName}.service.BaseEntityConfigService;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * 实体配置
 * @author Bob
 */
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
                String fullDomainName = "${packageName}.domain." + entityName;
                Class domainClass = Class.forName(fullDomainName);
                BaseDomain childDomain = (BaseDomain) domainClass.getConstructor().newInstance();
                // 设置实体对应的domain全限定名
                baseEntityConfigDTO.setFullDomainName(fullDomainName);
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
     * @throws Exception 异常
     */
    private void setTableName(BaseEntityConfigDTO baseEntityConfigDTO, String entityName, Class domainClass,
                              BaseDomain childDomain) throws Exception {
        // 获取表名（使用apache的包可以获取包括父类的属性）
        Field tableNameField = FieldUtils.getField(domainClass, "_TableName", true);
        if (tableNameField == null) {
            throw new Exception("${packageName}.domain." + entityName + "没有_TableName属性");
        }
        // 设置对象的访问权限，保证对private的属性的访问
        tableNameField.setAccessible(true);
        Object tableName = tableNameField.get(childDomain);
        if (tableName == null || "".equals(tableName.toString().trim())) {
            throw new Exception("${packageName}.domain." + entityName + "_TableName属性值为空");
        }
        baseEntityConfigDTO.setTableName(tableName.toString());
    }

}
