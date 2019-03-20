package ${packageName}.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import ${packageName}.config.GlobalCache;
import ${packageName}.domain.BaseDomain;
import ${packageName}.dto.BaseDTO;
import ${packageName}.dto.criteria.BaseCriteria;
import ${packageName}.dto.criteria.filter.*;
import ${packageName}.dto.help.*;
import ${packageName}.util.GenericsUtil;
import ${packageName}.util.MyBeanUtil;
import ${packageName}.util.StringUtil;
import ${packageName}.web.rest.errors.CommonException;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public interface BaseService<T extends BaseDomain, C extends BaseCriteria, O extends BaseDTO> extends IService<T> {

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
     * @param entityTypeName 实体类型简称
     * @param id 主键ID
     * @param baseCriteria 附加条件
     * @return 查询通用Wrapper
     */
    default Wrapper<T> idEqualsPrepare(String entityTypeName, Long id, BaseCriteria baseCriteria) {
        // 获取实体配置
        Class criteriaClass = GlobalCache.getCriteriaClassMap().get(entityTypeName);
        C criteria = null;
        try {
            criteria = (C)criteriaClass.newInstance();
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        MyBeanUtil.copyNonNullProperties(baseCriteria, criteria);
        Wrapper<T> wrapper = getWrapper(entityTypeName, null, criteria, null, null, null);
        ((QueryWrapper<T>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 获取查询数据的SQL
     * @param entityTypeName 实体类型简称
     * @param criteria 查询条件
     * @param tableIndexMap 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
     * @return
     */
    default String getDataQuerySql(String entityTypeName, C criteria, Map<String, String> tableIndexMap) {
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
     * @param entityTypeName 实体类型简称
     * @param criteria 查询条件
     * @param tableIndexMap 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
     * @return
     */
    default String getCountQuerySql(String entityTypeName, C criteria, Map<String, String> tableIndexMap) {
        int tableCount = 0;
        int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(entityTypeName, criteria, tableCount, fromTableCount, tableIndexMap);
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

    /**
     * 将criteria转换为wrapper
     * @param entityTypeName 实体类型名
     * @param wrapper 转换前或转换后的wrapper
     * @param criteria 转换前的条件
     * @param lastFieldName 最后的field名
     * @param tableIndexMap 表index的Map
     * @param normalCriteriaList 其他非框架的条件
     * @return 转换后的wrapper
     */
    default Wrapper<T> getWrapper(String entityTypeName, QueryWrapper<T> wrapper, C criteria, String lastFieldName,
                                  Map<String, String> tableIndexMap, List<NormalCriteriaDTO> normalCriteriaList) {
        // 获取实体配置
        BaseEntityConfigDTO entityConfig = GlobalCache.getEntityConfigMap().get(entityTypeName);
        // 是否调用的首栈（递归的第一次调用）
        boolean firstStackElement = false;
        if (wrapper == null) {
            firstStackElement = true;
            wrapper = new QueryWrapper<>();
            normalCriteriaList = new ArrayList<>();
        }
        // 获取表名字
        String tableName = entityConfig.getTableName();
        // 获取SQL中修改后的表名字
        if (tableIndexMap != null) {
            if (lastFieldName != null) {
                String tableIndexAndName = tableIndexMap.get(lastFieldName);
                if (tableIndexAndName != null) {
                    String tableIndex = tableIndexAndName.split("_")[0];
                    tableName = tableName + "_" + tableIndex;
                }
            } else {
                tableName = tableName + "_0";
            }
        }
        // 获取传入的查询条件的类和域
        Class criteriaClazz = criteria.getClass();
        // 数字的判断
        Pattern digitalPattern = Pattern.compile("^[-\\+]?[\\d]*$");
        // orderBy条件
        try {
            PropertyDescriptor pd = new PropertyDescriptor("orderBy", criteriaClazz);
            Method getMethod = pd.getReadMethod();
            Object result = getMethod.invoke(criteria);
            if (result instanceof String) {
                String[] orderBys = ((String)result).trim().split("\\,");
                for (String orderBy : orderBys) {
                    String subTableName = tableName;
                    if (tableIndexMap != null && orderBy.contains(".")) {
                        subTableName = getChangedTableName(orderBy, tableIndexMap);
                    }
                    String[] orderByDetail = orderBy.trim().split("\\s");
                    // 获取排序的实际字段名
                    String orderFieldName = orderByDetail[0];
                    if (orderFieldName.contains(".")) {
                        orderFieldName = orderFieldName.substring(orderFieldName.lastIndexOf(".") + 1);
                    }
                    if ("".equals(orderFieldName)) {
                        // 排序的字段名不得为空
                        continue;
                    }
                    // 获取排序的方向及顺序
                    boolean isAsc = true;
                    // 自定义排序的开始Index
                    // 默认index=0表示字段，index=1表示asc或desc，自定义排序规则从index=2开始
                    // 但是，如果参数传asc或desc，那么自定义排序规则从index=1开始
                    int customOrderIndexStart = 2;
                    // 排序方向字段（该字段数据也可能直接是自定义排序的第一项内容）
                    String orderDirection = "";
                    if (orderByDetail.length > 1) {
                        orderDirection = orderByDetail[1];
                        if ("".equals(orderDirection)) {
                            // 按正常规则顺序排序
                            isAsc = true;
                        } else {
                            if ("asc".equalsIgnoreCase(orderDirection) || "desc".equalsIgnoreCase(orderDirection)) {
                                // 加了asc或desc的排序
                                isAsc = "asc".equalsIgnoreCase(orderDirection);
                            } else {
                                // 没有加asc或desc的排序，默认asc排序，且orderDirection存放的内容变成了自定义排序的第一项
                                isAsc = true;
                                customOrderIndexStart = 1;
                            }
                        }
                    }
                    // 要排序的字段名（包括表名的别名）
                    String orderColumnName = subTableName + "." + StringUtil.camelToUnderline(orderFieldName);
                    // 自定义排序规则处理
                    if (orderByDetail.length > 2 || customOrderIndexStart == 1) {
                        String customFieldValueStr = "";
                        if (customOrderIndexStart == 1) {
                            customFieldValueStr += "," + (digitalPattern.matcher(orderDirection).matches() ?
                                    orderDirection : "'" + orderDirection + "'");
                        }
                        for (int i = 2; i < orderByDetail.length; i++) {
                            customFieldValueStr += "," + (digitalPattern.matcher(orderByDetail[i]).matches() ?
                                    orderByDetail[i] : "'" + orderByDetail[i] + "'");
                        }
                        orderColumnName = "field(" + orderColumnName + customFieldValueStr + ")";
                    }
                    wrapper.orderBy(true, isAsc, orderColumnName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 其他条件
        Field[] fields = FieldUtils.getAllFields(criteriaClazz);    // 使用apache的工具类可以获取类及父类的所有属性
        for (Field field : fields) {
            try {
                String fieldName = field.getName();
                String columnName = tableName + "." + StringUtil.camelToUnderline(fieldName);
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(fieldName, criteriaClazz);
                Method getMethod = pd.getReadMethod();
                Object result = getMethod.invoke(criteria);
                if (null == result) {
                    continue;
                }
                if (result instanceof StringFilter) {
                    if (((StringFilter)result).getContains() != null) {
                        wrapper.like(columnName, ((StringFilter) result).getContains());
                    }
                    if (((StringFilter)result).getNotContains() != null) {
                        wrapper.notLike(columnName, ((StringFilter) result).getNotContains());
                    }
                    if (((StringFilter)result).getStartWith() != null) {
                        wrapper.likeRight(columnName, ((StringFilter) result).getStartWith());
                    }
                    if (((StringFilter)result).getEndWith() != null) {
                        wrapper.likeLeft(columnName, ((StringFilter) result).getEndWith());
                    }
                }
                if (result instanceof BooleanFilter) {
                    // do nothing
                }
                if (result instanceof BigDecimalFilter) {
                    // do nothing
                }
                if (result instanceof DoubleFilter) {
                    // do nothing
                }
                if (result instanceof LongFilter) {
                    // do nothing
                }
                if (result instanceof IntegerFilter) {
                    // do nothing
                }
                if (result instanceof ShortFilter) {
                    // do nothing
                }
                if (result instanceof LocalDateFilter) {
                    // do nothing
                }
                if (result instanceof InstantFilter) {
                    // do nothing
                }
                if (result instanceof FloatFilter) {
                    // do nothing
                }
                if (result instanceof ZonedDateTimeFilter) {
                    // do nothing
                }
                if (result instanceof RangeFilter) {
                    // do nothing
                }
                // 是否框架使用的Filter或Criteria类型
                boolean filterOrCriteria = false;
                if (result instanceof Filter) {
                    filterOrCriteria = true;
                    if (((Filter)result).getEquals() != null) {
                        wrapper.eq(columnName, ((Filter)result).getEquals());
                    }
                    if (((Filter)result).getNotEquals() != null) {
                        wrapper.ne(columnName, ((Filter)result).getNotEquals());
                    }
                    if (((Filter)result).getNullable() != null) {
                        if (((Filter)result).getNullable()) {
                            wrapper.isNull(columnName);
                        } else {
                            wrapper.isNotNull(columnName);
                        }
                    }
                    if (((Filter)result).getIn() != null) {
                        wrapper.in(columnName, ((Filter)result).getIn());
                    }
                    if (((Filter)result).getNotIn() != null) {
                        wrapper.notIn(columnName, ((Filter)result).getNotIn());
                    }
                    if (((Filter)result).getGreaterThan() != null) {
                        wrapper.gt(columnName, ((Filter)result).getGreaterThan());
                    }
                    if (((Filter)result).getGreaterOrEqualThan() != null) {
                        wrapper.ge(columnName, ((Filter)result).getGreaterOrEqualThan());
                    }
                    if (((Filter)result).getLessThan() != null) {
                        wrapper.lt(columnName, ((Filter)result).getLessThan());
                    }
                    if (((Filter)result).getLessOrEqualThan() != null) {
                        wrapper.le(columnName, ((Filter)result).getLessOrEqualThan());
                    }
                    if (((Filter)result).getBetweenFrom() != null && ((Filter)result).getBetweenTo() != null) {
                        wrapper.between(columnName, ((Filter)result).getBetweenFrom(), ((Filter)result).getBetweenTo());
                    }
                    if (((Filter)result).getNotBetweenFrom() != null && ((Filter)result).getNotBetweenTo() != null) {
                        wrapper.notBetween(columnName, ((Filter)result).getNotBetweenFrom(), ((Filter)result).getNotBetweenTo());
                    }
                }
                if (result instanceof BaseCriteria) {
                    filterOrCriteria = true;
                }
                if (!filterOrCriteria) {
                    // 附加的其它类型的查询条件
                    if (!"associationNameList".equals(fieldName) && !"dictionaryNameList".equals(fieldName)
                            && !"sort".equals(fieldName) && !"page".equals(fieldName) && !"size".equals(fieldName)) {
                        // 除了系统框架的特殊属性外，其他都是普通属性
                        NormalCriteriaDTO normalCriteriaDTO = new NormalCriteriaDTO();
                        normalCriteriaDTO.setTableName(tableName);
                        normalCriteriaDTO.setFieldName(fieldName);
                        normalCriteriaDTO.setValue(result);
                        normalCriteriaList.add(normalCriteriaDTO);
                    }
                }
                if (result instanceof BaseCriteria) {
                    // join的实体
                    String typeName = result.getClass().getName();
                    String domainTypeName = typeName.substring(typeName.lastIndexOf(".") + 1, typeName.lastIndexOf("Criteria"));
                    // 级联的域名
                    String nowFieldName = lastFieldName == null ? fieldName : lastFieldName + "." + fieldName;
                    wrapper = (QueryWrapper<T>)GlobalCache.getServiceMap().get(domainTypeName).getWrapper(
                            domainTypeName, wrapper, (C)result, nowFieldName, tableIndexMap, normalCriteriaList);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new CommonException(e.getMessage());
            }
        }
        if (firstStackElement) {
            // 查询的主表
            if (tableIndexMap != null) {
                // 根据表别名反向查条件名的Map
                Map<String, String> revertTableIndexMap = new HashMap<>();
                for (Map.Entry<String, String> entry : tableIndexMap.entrySet()) {
                    String criteriaStr = entry.getKey();
                    String tableIndexAndName = entry.getValue();
                    String tableIndex = tableIndexAndName.split("_")[0];
                    String tableNameTmp = tableIndexAndName.split("_")[1];
                    tableNameTmp = StringUtil.camelToUnderline(tableNameTmp) + "_" + tableIndex;
                    revertTableIndexMap.put(tableNameTmp, criteriaStr);
                }
                revertTableIndexMap.put(tableName, "");
                // 在递归调用的首栈，增强条件查询
                wrapper = (QueryWrapper<T>) this.wrapperEnhance(wrapper, criteria, normalCriteriaList,
                        revertTableIndexMap);
            }
        }
        return wrapper;
    }

    /**
     * orderBy的字段自动先生成个空条件
     * @param criteria 查询条件
     * @param tableIndexMap 表index的Map
     * @return
     */
    default void preOrderBy(Object criteria, Map<String, String> tableIndexMap) {
        try {
            // 无任何条件的过滤器，占位用
            NothingFilter nothingFilter = new NothingFilter("none");
            Object criteriaIter = criteria;
            // 获取传入的查询条件的类和域
            Class criteriaClazz = criteria.getClass();
            PropertyDescriptor pd = new PropertyDescriptor("orderBy", criteriaClazz);
            Method getMethod = pd.getReadMethod();
            Object result = getMethod.invoke(criteria);
            if (result instanceof String) {
                String[] orderBys = ((String) result).trim().split("\\,");
                for (String orderBy : orderBys) {
                    if (tableIndexMap != null && orderBy.contains(".")) {
                        String changedTableName = getChangedTableName(orderBy, tableIndexMap);
                        if (changedTableName == null) {
                            // 获取不到值说明没有关于这个orderBy的条件查询，需要追加一个空的条件查询
                            String[] orderBySplit = orderBy.split("\\.");
                            for (int i = 0; i < orderBySplit.length; i++) {
                                Field orderByKeyField = criteriaIter.getClass().getDeclaredField(orderBySplit[i]);
                                orderByKeyField.setAccessible(true);
                                Object fieldValue = orderByKeyField.get(criteriaIter);
                                // 前面的都是BaseCriteria的子类对象
                                if (fieldValue == null) {
                                    fieldValue = Class.forName(orderByKeyField.getType().getName()).newInstance();
                                    orderByKeyField.set(criteriaIter, fieldValue);
                                }
                                if (i != orderBySplit.length - 1) {
                                    // 继续往下循环迭代
                                    criteriaIter = fieldValue;
                                } else {
                                    // 最后一个.后面就是具体字段名，是Filter的子类对象，追加nothingFilter
                                    if (fieldValue == null) {
                                        orderByKeyField.set(criteriaIter, nothingFilter);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 获取修改后的表名（追加_数字的表，用于区分同一SQL语句中的表）
     * @param cascadeEntityAndField 级联的实体和字段
     * @param tableIndexMap 表index的Map
     * @return
     */
    default String getChangedTableName(String cascadeEntityAndField, Map<String, String> tableIndexMap) {
        String changedTableName = null;
        String key = cascadeEntityAndField.substring(0, cascadeEntityAndField.lastIndexOf("."));
        String tableIndexAndName = tableIndexMap.get(key);
        if (tableIndexAndName != null) {
            // 已经加入过tableIndexMap
            String tableIndex = tableIndexAndName.split("_")[0];
            String domainTypeName = tableIndexAndName.split("_")[1];
            // 获取实体配置
            BaseEntityConfigDTO entityConfig = GlobalCache.getEntityConfigMap().get(domainTypeName);
            changedTableName = entityConfig.getTableName();
            changedTableName = changedTableName + "_" + tableIndex;
        }
        return changedTableName;
    }

    /**
     * 获取关联属性
     * @param entityTypeName 实体类型名
     * @param dto 主实体
     * @param criteria 关联属性的条件
     * @param appendParamMap 附加的查询参数条件
     * @return 带关联属性的主实体
     */
    default O getAssociations(String entityTypeName, O dto, BaseCriteria criteria, Map<String, Object> appendParamMap) {
        if (dto.getId() == null) {
            return dto;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList == null || associationNameList.size() == 0) {
            return dto;
        }
        List<BaseEntityConfigRelationDTO> relationList = GlobalCache.getEntityRelationsMap().get(entityTypeName);
        if (relationList == null || relationList.size() == 0) {
            return dto;
        }
        // 获取级联的参数请求
        for (String associationName : associationNameList) {
            for (BaseEntityConfigRelationDTO relationDTO : relationList) {
                try {
                    if ("from".equals(relationDTO.getFromOrTo())) {
                        // 级联获取List（下级）的内容
                        String associationFromDTO = "OneToOne".equals(relationDTO.getRelationType()) ?
                                relationDTO.getFromName() : relationDTO.getFromName() + "List";
                        if (!associationFromDTO.equals(associationName)) {
                            // 级联查询名称不符合，跳过
                            continue;
                        }
                        // 级联查询名称符合
                        // 继续设置下级的级联查询
                        List<String> subAssociationNameList = new ArrayList<>();
                        for (String associationNameIter : associationNameList) {
                            if (associationNameIter.startsWith(associationName + ".")) {
                                String subAssociationName = associationNameIter.substring((associationName + ".").length());
                                subAssociationNameList.add(subAssociationName);
                            }
                        }
                        // 设置级联查询的查询条件
                        Class subCriteriaClass = Class.forName("${packageName}.dto.criteria." + relationDTO.getToType() + "Criteria");
                        BaseCriteria subCriteria = (BaseCriteria) subCriteriaClass.newInstance();
                        LongFilter relatedIdFilter = new LongFilter();
                        relatedIdFilter.setEquals(dto.getId());
                        Field relatedIdField = FieldUtils.getField(subCriteriaClass, relationDTO.getToName() + "Id", true);
                        relatedIdField.setAccessible(true);
                        relatedIdField.set(subCriteria, relatedIdFilter);
                        subCriteria.setAssociationNameList(subAssociationNameList);
                        // 调用级联的Service的方法进行查询
                        Object subDTOList = GlobalCache.getServiceMap().get(relationDTO.getToType())
                                .findAll(subCriteria, appendParamMap).getData();
                        // 最终设置到主体dto的成员变量中
                        if ("OneToOne".equals(relationDTO.getRelationType())) {
                            // 一对一
                            Field dtoField = FieldUtils.getField(dto.getClass(), relationDTO.getFromName(), true);
                            dtoField.set(dto, (subDTOList == null || ((List) subDTOList).size() == 0) ? null : ((List) subDTOList).get(0));
                        } else {
                            // 一对多
                            Field dtoField = FieldUtils.getField(dto.getClass(), relationDTO.getFromName() + "List", true);
                            dtoField.set(dto, subDTOList == null ? null : (List) subDTOList);
                        }
                        break;
                    } else {
                        // 级联获取上级的内容
                        String associationFromDTO = relationDTO.getToName();
                        if (!associationFromDTO.equals(associationName)) {
                            continue;
                        }
                        // 级联查询名称符合
                        // 继续设置上级的级联查询
                        Field relatedIdField = FieldUtils.getField(dto.getClass(), relationDTO.getToName() + "Id", true);
                        Object relatedId = relatedIdField.get(dto);
                        if (relatedId == null) {
                            break;
                        }
                        List<String> subAssociationNameList = new ArrayList<>();
                        for (String associationNameIter : associationNameList) {
                            if (associationNameIter.startsWith(associationName + ".")) {
                                String subAssociationName = associationNameIter.substring((associationName + ".").length());
                                subAssociationNameList.add(subAssociationName);
                            }
                        }
                        // 设置级联查询的查询条件
                        Class subCriteriaClass = Class.forName("${packageName}.dto.criteria." + relationDTO.getFromType() + "Criteria");
                        BaseCriteria subCriteria = (BaseCriteria) subCriteriaClass.newInstance();
                        subCriteria.setAssociationNameList(subAssociationNameList);
                        // 调用级联的Service的方法进行查询
                        Object subDTO = GlobalCache.getServiceMap().get(relationDTO.getFromType()).findOne(
                                (long)relatedId, subCriteria, appendParamMap).getData();
                        // 最终设置到主体dto的成员变量中
                        Field dtoField = FieldUtils.getField(dto.getClass(), relationDTO.getToName(), true);
                        dtoField.set(dto, subDTO);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return dto;
    }

    default ReturnCommonDTO save(O dto) {
        return new ReturnCommonDTO<>();
    }

    default ReturnCommonDTO deleteById(Long id) {
        return new ReturnCommonDTO<>();
    }

    default ReturnCommonDTO deleteByIdList(List<Long> idList) {
        return new ReturnCommonDTO<>();
    }

    default ReturnCommonDTO deleteByIdListNot(List<Long> idList) {
        return new ReturnCommonDTO<>();
    }

    default ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap) {
        return new ReturnCommonDTO<>();
    }

    default ReturnCommonDTO<O> findOne(Long id, BaseCriteria criteria, Map<String, Object> appendParamMap) {
        return new ReturnCommonDTO<>();
    }

    default ReturnCommonDTO<List<O>> findAll(C criteria, Map<String, Object> appendParamMap) {
        return new ReturnCommonDTO<>();
    }

    default ReturnCommonDTO<IPage<O>> findPage(C criteria, MbpPage pageable, Map<String, Object> appendParamMap) {
        return new ReturnCommonDTO<>();
    }

    default ReturnCommonDTO<Integer> findCount(C criteria) {
        return new ReturnCommonDTO<>();
    }

}
