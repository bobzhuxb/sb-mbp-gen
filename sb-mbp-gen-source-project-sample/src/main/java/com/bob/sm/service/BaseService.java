package com.bob.sm.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bob.sm.config.GlobalCache;
import com.bob.sm.domain.BaseDomain;
import com.bob.sm.dto.criteria.BaseCriteria;
import com.bob.sm.dto.help.BaseEntityConfigDTO;
import com.bob.sm.dto.help.BaseEntityConfigDicDTO;
import com.bob.sm.dto.help.NormalCriteriaDTO;
import com.bob.sm.util.GenericsUtil;
import com.bob.sm.util.MbpUtil;
import com.bob.sm.util.MyBeanUtil;
import com.bob.sm.web.rest.errors.CommonException;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface BaseService<T extends BaseDomain, C extends BaseCriteria> extends IService<T> {

    /**
     * 附加的条件查询增强方法，实现类可覆盖该方法，写自己的条件查询增强方法
     * @param wrapper 增强前的Wrapper条件
     * @param criteria 原始的查询条件
     * @param normalCriteriaList 普通的查询条件
     * @param revertTableIndexMap 根据表别名反向查条件名的Map
     * @return 增强后的Wrapper条件
     */
    default <C> Wrapper<T> wrapperEnhance(QueryWrapper<T> wrapper, C criteria, List<NormalCriteriaDTO> normalCriteriaList,
                                          Map<String, String> revertTableIndexMap) {
        return wrapper;
    }

    /**
     * 根据ID查询的条件准备
     * @param id 主键ID
     * @param baseCriteria 附加条件
     * @return 查询通用Wrapper
     */
    default Wrapper<T> idEqualsPrepare(Long id, BaseCriteria baseCriteria) {
        Class<T> domainClass = GenericsUtil.getSuperClassGenricType(this.getClass(), 1);
        C criteria = null;
        try {
            // 根据泛型创建对象
            Class criteriaClass = Class.forName("com.bob.sm.dto.criteria." + domainClass.getSimpleName() + "Criteria");
            criteria = (C)criteriaClass.newInstance();
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        MyBeanUtil.copyNonNullProperties(baseCriteria, criteria);
        Wrapper<T> wrapper = MbpUtil.getWrapper(null, criteria, domainClass, null, null, this, null);
        ((QueryWrapper<T>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 获取查询数据的SQL
     * @param criteria 查询条件
     * @param tableIndexMap 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
     * @return
     */
    default String getDataQuerySql(C criteria, Map<String, String> tableIndexMap) {
        // 获取第一个泛型参数的类（BaseDomain的子类）
        Class domainClass = GenericsUtil.getSuperClassGenricType(this.getClass(), 1);
        String entityTypeName = domainClass.getSimpleName();
        // 获取实体配置
        BaseEntityConfigDTO entityConfig = GlobalCache.getEntityConfigMap().get(entityTypeName);
        int tableCount = 0;
        int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + entityConfig.getTableName() + "_" + tableCount + ".*";
        List<BaseEntityConfigDicDTO> entityConfigDicList = GlobalCache.getEntityDicNameMap().get(entityTypeName);
        if (entityConfigDicList != null) {
            // 处理关联数据字典值
            List<String> dictionaryNameList = criteria.getDictionaryNameList();
            if (dictionaryNameList != null) {
                // 此处处理数据字典的JOIN
                for (BaseEntityConfigDicDTO entityConfigDicDTO : entityConfigDicList) {
                    if (dictionaryNameList.contains(entityConfigDicDTO.getFieldValueName())) {
                        joinDataSql += " ,base_dictionary_" + entityConfigDicDTO.getColumnName() + "_" + tableCount
                                + ".dic_value AS " + entityConfigDicDTO.getColumnValueName();
                    }
                }
            }
        }
        joinDataSql += getFromAndJoinSql(entityTypeName, criteria, tableCount, fromTableCount, tableIndexMap);
        return joinDataSql;
    }

    /**
     * 获取查询数量的SQL
     * @param criteria 查询条件
     * @param tableIndexMap 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
     * @return
     */
    default String getCountQuerySql(C criteria, Map<String, String> tableIndexMap) {
        // 获取第一个泛型参数的类（BaseDomain的子类）
        Class domainClass = GenericsUtil.getSuperClassGenricType(this.getClass(), 1);
        String entityName = domainClass.getSimpleName();
        int tableCount = 0;
        int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(entityName, criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @param entityTypeName 实体类型简称
     * @param criteria 查询条件
     * @param tableCount 当前表序号
     * @param fromTableCount 上一层的表序号
     * @param tableIndexMap 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
     * @return
     */
    default String getFromAndJoinSql(String entityTypeName, C criteria, int tableCount, int fromTableCount,
                                     Map<String, String> tableIndexMap) {
        // 获取实体配置
        BaseEntityConfigDTO entityConfig = GlobalCache.getEntityConfigMap().get(entityTypeName);
        String joinSubSql = " FROM " + entityConfig.getTableName() + " AS " + entityConfig.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(entityTypeName, criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @param entityTypeName 实体类型简称
     * @param criteria 查询条件
     * @param tableCount 当前表序号
     * @param fromTableCount 上一层的表序号
     * @param lastFieldName 上一级查询的最后字段名
     * @param tableIndexMap 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
     * @return
     */
    default String getJoinSql(String entityTypeName, C criteria, int tableCount, int fromTableCount, String lastFieldName,
                              Map<String, String> tableIndexMap) {
        String joinSubSql = "";
        // 获取实体配置
        BaseEntityConfigDTO entityConfig = GlobalCache.getEntityConfigMap().get(entityTypeName);
        // 获取数据字典配置
        List<BaseEntityConfigDicDTO> entityConfigDicList = GlobalCache.getEntityDicNameMap().get(entityTypeName);
        // 处理关联数据字典值
        if (entityConfigDicList != null) {
            List<String> dictionaryNameList = criteria.getDictionaryNameList();
            if (dictionaryNameList != null) {
                // 此处处理数据字典的JOIN
                for (BaseEntityConfigDicDTO entityConfigDicDTO : entityConfigDicList) {
                    if (dictionaryNameList.contains(entityConfigDicDTO.getFieldValueName())) {
                        joinSubSql += " LEFT JOIN base_dictionary AS base_dictionary_" + entityConfigDicDTO.getColumnName()
                                + "_" + tableCount + " ON base_dictionary_" + entityConfigDicDTO.getColumnName() + "_" + tableCount
                                + ".dic_type = '" + entityConfigDicDTO.getDicType() + "' AND base_dictionary_"
                                + entityConfigDicDTO.getColumnName() + "_" + tableCount + ".dic_code = "
                                + entityConfig.getTableName() + "_" + fromTableCount + "." + entityConfigDicDTO.getColumnName();
                    }
                }
            }
        }
        // 处理级联查询
        // 使用apache的包可以获取包括父类的属性
        Field[] criteriaFields = FieldUtils.getAllFields(criteria.getClass());
        // 获取第一个泛型参数的类（BaseDomain的子类）
        Class domainClass = GenericsUtil.getSuperClassGenricType(this.getClass(), 1);
        // 遍历Criteria的成员变量
        for (Field criteriaField : criteriaFields) {
            // 设置对象的访问权限，保证对private的属性的访问
            criteriaField.setAccessible(true);
            String fieldName = criteriaField.getName();     // 属性名
            String fieldTypeName = criteriaField.getType().getSimpleName();       // 属性的类型名
            Object criteriaFieldObj = null;
            try {
                criteriaFieldObj = criteriaField.get(criteria);
            } catch (Exception e) {
                throw new CommonException("获取字段" + fieldName + "异常");
            }
            // 只处理BaseCriteria子类对象的属性
            if (criteriaFieldObj != null && criteriaFieldObj instanceof BaseCriteria) {
                // 去掉Criteria的名，即对应的实体类型名
                String fieldDomainName = fieldTypeName.substring(0, fieldTypeName.length() - 8);
                // 获取属性对应的实体配置
                BaseEntityConfigDTO relatedEntityConfig = GlobalCache.getEntityConfigMap().get(fieldDomainName);
                // 使用的表统计数
                tableCount++;
                // 下级级联只设置tableIndexMap数据，不添加查询SQL
                if (!fieldName.endsWith("List")) {
                    // 上级级联需要设置tableIndexMap数据，并且添加查询SQL
                    Object joinColumnName = null;
                    try {
                        Field columnNameField = FieldUtils.getField(domainClass, "_" + fieldName + "Id", true);
                        columnNameField.setAccessible(true);
                        joinColumnName = columnNameField.get(domainClass);
                    } catch (Exception e) {
                        throw new CommonException("获取字段_" + fieldName + "Id异常");
                    }
                    if (joinColumnName == null) {
                        throw new CommonException("字段_" + fieldName + "Id值为空");
                    }
                    // 上级级联
                    joinSubSql += " LEFT JOIN " + relatedEntityConfig.getTableName() + " AS "
                            + relatedEntityConfig.getTableName() + "_" + tableCount + " ON "
                            + relatedEntityConfig.getTableName() + "_" + tableCount + ".id = "
                            + entityConfig.getTableName() + "_" + fromTableCount
                            + "." + joinColumnName;
                }
                String tableKey = fieldName;
                if (lastFieldName != null) {
                    // 拼接key
                    tableKey = lastFieldName + "." + tableKey;
                }
                tableIndexMap.put(tableKey, tableCount + "_" + fieldDomainName);
                String nextJoinSql = GlobalCache.getServiceMap().get(fieldDomainName).getJoinSql(fieldDomainName,
                        (BaseCriteria)criteriaFieldObj, tableCount, tableCount, tableKey, tableIndexMap);
                if (!fieldName.endsWith("List")) {
                    // 上级级联
                    joinSubSql += nextJoinSql;
                }
            }
        }
        return joinSubSql;
    }

}
