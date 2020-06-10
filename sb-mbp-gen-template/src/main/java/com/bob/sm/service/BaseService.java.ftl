package ${packageName}.service;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ${packageName}.config.Constants;
import ${packageName}.config.GlobalCache;
import ${packageName}.domain.SystemDictionary;
import ${packageName}.domain.BaseDomain;
import ${packageName}.dto.BaseDTO;
import ${packageName}.dto.SystemUserDTO;
import ${packageName}.dto.criteria.BaseCriteria;
import ${packageName}.dto.criteria.SystemUserCriteria;
import ${packageName}.dto.criteria.filter.*;
import ${packageName}.dto.help.*;
import ${packageName}.util.GenericsUtil;
import ${packageName}.util.MyBeanUtil;
import ${packageName}.util.MyStringUtil;
import ${packageName}.web.rest.errors.CommonAlertException;
import ${packageName}.web.rest.errors.CommonException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.springframework.aop.framework.AopContext;

import org.springframework.transaction.annotation.Transactional;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service的共通方法实现
 * @author Bob
 */
public interface BaseService<T extends BaseDomain, C extends BaseCriteria, O extends BaseDTO> extends IService<T> {

    /**
     * 记录日志用
     * @return
     */
    Logger getLog();

    /**
     * 新增修改验证（具体子类实现）
     * @param dto 主实体
     * @param appendMap 附加的参数
     * @return 是否继续执行保存
     */
    default boolean baseSaveValidator(O dto, Map<String, Object> appendMap) {
        // TODO: 新增修改验证写在这里（由具体实现覆盖）
        return true;
    }

    /**
     * 新增修改之后的操作（具体子类实现）
     * @param dto 主实体
     * @param appendMap 附加的参数（前面处理过的结果）
     */
    default void baseDoAfterSave(O dto, Map<String, Object> appendMap) {
        // TODO: 新增修改之后的操作写在这里（由具体实现覆盖）
    }

    /**
     * 新增修改最终返回前的操作（具体子类实现）
     * @param dto 主实体
     * @param appendMap 附加的参数（前面处理过的结果）
     */
    default void baseDoBeforeSaveReturn(O dto, Map<String, Object> appendMap) {
        // TODO: 新增修改之后的操作写在这里（由具体实现覆盖）
    }

    /**
     * 单条删除执行完毕之后，在事务外做的非事务操作
     * @param id 删除的数据的主键ID
     * @param appendMap 附加的参数（前面处理过的结果）
     */
    default void baseDoAfterDeleteByIdOutTrans(String id, Map<String, Object> appendMap) {
        // TODO: 单条删除之后的外部非事务操作写在这里（由具体实现覆盖）
    }

    /**
     * 新增或修改操作执行完毕之后，在事务外做的非事务操作
     * @param dto 操作的数据
     * @param appendMap 附加的参数（前面处理过的结果）
     */
    default void baseDoAfterSaveOutTrans(O dto, Map<String, Object> appendMap) {
        // TODO: 新增修改之后的外部非事务操作写在这里（由具体实现覆盖）
    }

    /**
     * 删除验证（具体子类实现）
     * @param domain 数据库中查询出的实体数据内容
     * @param appendMap 附加的传递参数
     */
    default void baseDeleteValidator(T domain, Map<String, Object> appendMap) {
        // TODO: 删除验证写在这里（由具体实现覆盖）
    }

    /**
     * 附加的条件查询增强方法，实现类可覆盖该方法，写自己的条件查询增强方法
     * @param wrapper 增强前的Wrapper条件
     * @param criteria 原始的查询条件
     * @param appendParamMap 附加的传递参数
     * @param normalCriteriaList 普通的查询条件
     * @param revertTableIndexMap 根据表别名反向查条件名的Map
     * @return 增强后的Wrapper条件
     */
    default Wrapper<T> baseWrapperEnhance(QueryWrapper<T> wrapper, C criteria, Map<String, Object> appendParamMap,
                                          List<NormalCriteriaDTO> normalCriteriaList, Map<String, String> revertTableIndexMap) {
        // TODO: 附加的条件查询写在这里（由具体实现覆盖）
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param criteria 附加条件
     * @param appendParamMap 附加的传递参数
     * @param interceptReturnInfo 拦截的返回信息
     * @return 是否有权限（true：有权限  false：无权限）
     */
    default boolean baseDataAuthorityFilter(C criteria, Map<String, Object> appendParamMap, ReturnCommonDTO interceptReturnInfo) {
        // TODO: 数据权限的过滤写在这里（由具体实现覆盖）
        return true;
    }

    /**
     * 获取其他关联属性（前）
     * @param dto 主实体
     * @param criteria 关联属性的条件
     * @param appendParamMap 附加的查询参数条件
     * @return 带关联属性的主实体
     */
    default O baseGetAssociationsPrev(O dto, C criteria, Map<String, Object> appendParamMap) {
        // TODO: 获取其他的关联属性写在这里（由具体实现覆盖）
        return dto;
    }

    /**
     * 获取其他关联属性（后）
     * @param dto 主实体
     * @param criteria 关联属性的条件
     * @param appendParamMap 附加的查询参数条件
     * @return 带关联属性的主实体
     */
    default O baseGetAssociationsNext(O dto, C criteria, Map<String, Object> appendParamMap) {
        // TODO: 获取其他的关联属性写在这里（由具体实现覆盖）
        return dto;
    }

    /**
     * 删除某关联ID条件下，且不在本实体主键ID列表中的数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param entityTypeName 实体类型简称
     * @param relatedColumnName 关联的字段名称
     * @param relatedId 关联的ID
     * @param idList 主键ID列表
     * @param appendMap 附加的传递参数
     * @return 结果返回码和消息
     * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO baseDeleteByRelationIdWithoutIdList(String entityTypeName, String relatedColumnName,
                                                                String relatedId, List<String> idList,
                                                                Map<String, Object> appendMap) {
        Optional.ofNullable(GlobalCache.getMapperMap().get(entityTypeName).selectList(
                new QueryWrapper<T>().select("id").eq(relatedColumnName, relatedId).notIn(
                        idList != null && idList.size() > 0, "id", idList))
        ).get().stream().forEach(domain -> {
            ReturnCommonDTO returnCommonDTO = ((BaseService)AopContext.currentProxy()).baseDeleteByMapCascade(
                    entityTypeName, new HashMap<String, Object>() {{
                put("id", ((BaseDomain) domain).getId());
            }}, appendMap);
            if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
                throw new CommonAlertException(returnCommonDTO.getErrMsg());
            }
        });
        return new ReturnCommonDTO();
    }

    /**
     * 根据ID查询的条件准备
     * @param entityTypeName 实体类型简称
     * @param id 主键ID
     * @param baseCriteria 附加条件
     * @param appendParamMap 附加的传递参数
     * @return 查询通用Wrapper
     */
    default Wrapper<T> baseIdEqualsPrepare(String entityTypeName, String id, BaseCriteria baseCriteria,
                                           Map<String, Object> appendParamMap) {
        if (appendParamMap == null) {
            appendParamMap = new HashMap<>();
        }
        // 获取实体配置
        Class criteriaClass = GlobalCache.getCriteriaClassMap().get(entityTypeName);
        C criteria = null;
        try {
            criteria = (C)criteriaClass.newInstance();
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        MyBeanUtil.copyNonNullProperties(baseCriteria, criteria);
        Wrapper<T> wrapper = baseGetWrapper(entityTypeName, null, criteria, appendParamMap, null, null, null);
        ((QueryWrapper<T>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 获取最终的查询列
     * @param criteria 查询条件
     * @param entityPath 实体的上层路径
     */
    default List<String> baseGetFinalQueryColumnList(C criteria, String entityPath) {
        if (entityPath == null) {
            entityPath = "";
        }
        // 最终查询的列，存放的内容是数据库中表的列名
        List<String> finalQueryColumnList = new ArrayList<>();
        // 名称为sqlColumn，实际上存放的仍然是Java的实体名
        // 此处只会被baseFindAll和baseFindPage直接使用，所以此处用的是第一层级的sqlColumn
        List<String> sqlColumnList = criteria.getSqlColumnList();
        if (sqlColumnList != null && sqlColumnList.size() > 0) {
            for (String sqlColumn : sqlColumnList) {
                if ("".equals(entityPath)) {
                    // 第一层级，列名不包含.
                    if (!sqlColumn.contains(".")) {
                        finalQueryColumnList.add(MyStringUtil.camelToUnderline(sqlColumn));
                    }
                } else {
                    // 其他层级，列名以entityPath开头，且之后不包含.符号
                    if (sqlColumn.startsWith(entityPath)) {
                        String lastColumn = sqlColumn.substring(entityPath.length());
                        if (!lastColumn.contains(".")) {
                            finalQueryColumnList.add(MyStringUtil.camelToUnderline(lastColumn));
                        }
                    }
                }
            }
        }
        return finalQueryColumnList;
    }

    /**
     * 获取查询数据的SQL
     * @param entityTypeName 实体类型简称
     * @param criteria 查询条件
     * @param tableIndexMap 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
     * @param entityPath 实体的上层路径
     * @return
     */
    default String baseGetDataQuerySql(String entityTypeName, C criteria, Map<String, String> tableIndexMap,
                                       String entityPath) {
        // 获取实体配置
        BaseEntityConfigDTO entityConfig = GlobalCache.getEntityConfigMap().get(entityTypeName);
        int tableCount = 0;
        int fromTableCount = tableCount;
        // 获取当前表的别名
        String currentFullTableAlias = entityConfig.getTableName() + "_" + tableCount;
        // 开始生成SQL语句
        String joinDataSql = "SELECT ";
        // 最终查询的列，存放的内容是数据库中表的列名
        List<String> finalQueryColumnList = baseGetFinalQueryColumnList(criteria, entityPath);
        if (finalQueryColumnList.size() == 0) {
            // 如果不设置，默认查询全部列
            joinDataSql += currentFullTableAlias + ".*";
        } else {
            StringBuffer columnSb = new StringBuffer();
            boolean idColumnExist = false;
            for (String finalQueryColumn : finalQueryColumnList) {
                if ("id".equals(finalQueryColumn)) {
                    idColumnExist = true;
                }
                columnSb.append(currentFullTableAlias + "." + finalQueryColumn + ", ");
            }
            String columnStr = columnSb.toString().trim();
            if (!idColumnExist) {
                // 主键ID必须存在
                columnStr = currentFullTableAlias + ".id, " + columnStr;
            }
            joinDataSql += columnStr.substring(0, columnStr.length() - 1);
        }
        // 获取其他配置
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
        joinDataSql += baseGetFromAndJoinSql(entityTypeName, criteria, tableCount, fromTableCount, tableIndexMap);
        return joinDataSql;
    }

    /**
     * 获取查询数量的SQL
     * @param entityTypeName 实体类型简称
     * @param criteria 查询条件
     * @param tableIndexMap 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
     * @return
     */
    default String baseGetCountQuerySql(String entityTypeName, C criteria, Map<String, String> tableIndexMap) {
        int tableCount = 0;
        int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + baseGetFromAndJoinSql(entityTypeName, criteria, tableCount, fromTableCount, tableIndexMap);
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
    default String baseGetFromAndJoinSql(String entityTypeName, C criteria, int tableCount, int fromTableCount,
                                         Map<String, String> tableIndexMap) {
        // 获取实体配置
        BaseEntityConfigDTO entityConfig = GlobalCache.getEntityConfigMap().get(entityTypeName);
        String joinSubSql = " FROM " + entityConfig.getTableName() + " AS " + entityConfig.getTableName() + "_" + tableCount;
        joinSubSql += baseGetJoinSql(entityTypeName, criteria, tableCount, fromTableCount, null, tableIndexMap);
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
    default String baseGetJoinSql(String entityTypeName, C criteria, int tableCount, int fromTableCount, String lastFieldName,
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
                String nextJoinSql = GlobalCache.getServiceMap().get(fieldDomainName).baseGetJoinSql(fieldDomainName,
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
     * @param appendParamMap 附加的传递参数
     * @param lastFieldName 最后的field名
     * @param tableIndexMap 表index的Map
     * @param normalCriteriaList 其他非框架的条件
     * @return 转换后的wrapper
     */
    default Wrapper<T> baseGetWrapper(String entityTypeName, QueryWrapper<T> wrapper, C criteria,
                                      Map<String, Object> appendParamMap, String lastFieldName,
                                      Map<String, String> tableIndexMap, List<NormalCriteriaDTO> normalCriteriaList) {
        if (appendParamMap == null) {
            appendParamMap = new HashMap<>();
        }
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
        Pattern digitalPattern = Pattern.compile(Constants.REGEX_INTEGER_ALL);
        // orderBy条件
        try {
            PropertyDescriptor pd = new PropertyDescriptor("orderBy", criteriaClazz);
            Method getMethod = pd.getReadMethod();
            Object result = getMethod.invoke(criteria);
            // 是否直接使用
            PropertyDescriptor pdDirectOrderBy = new PropertyDescriptor("useDirectOrderBy", criteriaClazz);
            Method getMethodDirectOrderBy = pdDirectOrderBy.getReadMethod();
            Object resultDirectOrderBy = getMethodDirectOrderBy.invoke(criteria);
            if (result instanceof String) {
                if (resultDirectOrderBy instanceof Integer && Constants.yesNo.YES.getValue().equals(resultDirectOrderBy)) {
                    // 直接设置orderBy
                    wrapper.last("ORDER BY " + result);
                } else {
                    // 通过处理设置orderBy
                    String[] orderBys = ((String) result).trim().split("\\,");
                    for (String orderBy : orderBys) {
                        String subTableName = tableName;
                        if (tableIndexMap != null && orderBy.contains(".")) {
                            subTableName = baseGetChangedTableName(orderBy, tableIndexMap);
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
                        String orderColumnName = subTableName + "." + MyStringUtil.camelToUnderline(orderFieldName);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 其他条件
        Field[] fields = FieldUtils.getAllFields(criteriaClazz);    // 使用apache的工具类可以获取类及父类的所有属性
        for (Field field : fields) {
            try {
                String fieldName = field.getName();
                String columnName = tableName + "." + MyStringUtil.camelToUnderline(fieldName);
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
                        wrapper.and(i -> i.like(columnName, ((StringFilter) result).getContains()
                                .replace("/", "//").replace("_", "/_").replace("%", "/%"))
                                .last("ESCAPE '/'"));
                    }
                    if (((StringFilter)result).getNotContains() != null) {
                        wrapper.and(i -> i.notLike(columnName, ((StringFilter) result).getContains()
                                .replace("/", "//").replace("_", "/_").replace("%", "/%"))
                                .last("ESCAPE '/'"));
                    }
                    if (((StringFilter)result).getStartWith() != null) {
                        wrapper.and(i -> i.likeRight(columnName, ((StringFilter) result).getContains()
                                .replace("/", "//").replace("_", "/_").replace("%", "/%"))
                                .last("ESCAPE '/'"));
                    }
                    if (((StringFilter)result).getEndWith() != null) {
                        wrapper.and(i -> i.likeLeft(columnName, ((StringFilter) result).getContains()
                                .replace("/", "//").replace("_", "/_").replace("%", "/%"))
                                .last("ESCAPE '/'"));
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
                    if (((Filter)result).getExists() != null) {
                        for (Object existSql : ((Filter)result).getExists()) {
                            wrapper.exists((String)existSql);
                        }
                    }
                    if (((Filter)result).getNotExists() != null) {
                        for (Object notExistSql : ((Filter)result).getNotExists()) {
                            wrapper.notExists((String)notExistSql);
                        }
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
                    wrapper = (QueryWrapper<T>)GlobalCache.getServiceMap().get(domainTypeName).baseGetWrapper(
                            domainTypeName, wrapper, (C)result, appendParamMap, nowFieldName, tableIndexMap,
                            normalCriteriaList);
                }
            } catch (Exception e) {
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
                    tableNameTmp = MyStringUtil.camelToUnderline(tableNameTmp) + "_" + tableIndex;
                    revertTableIndexMap.put(tableNameTmp, criteriaStr);
                }
                revertTableIndexMap.put(tableName, "");
                // 在递归调用的首栈，增强条件查询
                wrapper = (QueryWrapper<T>) this.baseWrapperEnhance(wrapper, criteria, appendParamMap, normalCriteriaList,
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
    default void basePreOrderBy(Object criteria, Map<String, String> tableIndexMap) {
        try {
            // 无任何条件的过滤器，占位用
            NothingFilter nothingFilter = new NothingFilter("none");
            Object criteriaIter = criteria;
            // 获取传入的查询条件的类和域
            Class criteriaClazz = criteria.getClass();
            PropertyDescriptor pdOrderBy = new PropertyDescriptor("orderBy", criteriaClazz);
            Method getMethodOrderBy = pdOrderBy.getReadMethod();
            Object resultOrderBy = getMethodOrderBy.invoke(criteria);
            // 是否直接使用
            PropertyDescriptor pdDirectOrderBy = new PropertyDescriptor("useDirectOrderBy", criteriaClazz);
            Method getMethodDirectOrderBy = pdDirectOrderBy.getReadMethod();
            Object resultDirectOrderBy = getMethodDirectOrderBy.invoke(criteria);
            // 获取排序所涉及的表
            PropertyDescriptor pdAppendRelated = new PropertyDescriptor("appendRelated", criteriaClazz);
            Method getMethodAppendRelated = pdAppendRelated.getReadMethod();
            Object resultAppendRelated = getMethodAppendRelated.invoke(criteria);
            if (resultAppendRelated instanceof String) {
                // 获取相关表
                baseProcessOrderBy(tableIndexMap, resultAppendRelated, criteriaIter, nothingFilter);
            }
            if (resultOrderBy instanceof String) {
                // 通过处理设置orderBy
                if (resultDirectOrderBy instanceof Integer && Constants.yesNo.YES.getValue().equals(resultDirectOrderBy)) {
                    // 直接把orderBy放到SQL语句中的情况，不处理
                } else {
                    baseProcessOrderBy(tableIndexMap, resultOrderBy, criteriaIter, nothingFilter);
                }
            }
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 处理order by所涉及到的表
     * @param tableIndexMap
     * @param result
     * @param criteriaIter
     * @param nothingFilter
     * @throws Exception
     */
    default void baseProcessOrderBy(Map<String, String> tableIndexMap, Object result,
                                    Object criteriaIter, NothingFilter nothingFilter) throws Exception {
        // 通过处理设置orderBy
        String[] orderBys = ((String) result).trim().split("\\,");
        for (String orderBy : orderBys) {
            if (tableIndexMap != null && orderBy.contains(".")) {
                String changedTableName = baseGetChangedTableName(orderBy, tableIndexMap);
                if (changedTableName == null) {
                    // 获取不到值说明没有关于这个orderBy的条件查询，需要追加一个空的条件查询
                    String[] orderBySplit = orderBy.split("\\.");
                    for (int i = 0; i < orderBySplit.length; i++) {
                        // 如果最后一段有空格（例如后面跟desc）,需要截取前面的部分
                        String orderFieldName = orderBySplit[i];
                        if (orderFieldName.contains(" ")) {
                            orderFieldName = orderFieldName.split(" ")[0];
                        }
                        Field orderByKeyField = ReflectUtil.getField(criteriaIter.getClass(), orderFieldName);
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

    /**
     * 获取修改后的表名（追加_数字的表，用于区分同一SQL语句中的表）
     * @param cascadeEntityAndField 级联的实体和字段
     * @param tableIndexMap 表index的Map
     * @return
     */
    default String baseGetChangedTableName(String cascadeEntityAndField, Map<String, String> tableIndexMap) {
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
     * @param entityPath 实体的上层路径
     * @param appendParamMap 附加的查询参数条件
     * @return 带关联属性的主实体
     */
    @Transactional(rollbackFor = Exception.class)
    default O baseGetAssociations(String entityTypeName, O dto, BaseCriteria criteria, String entityPath,
                                  Map<String, Object> appendParamMap) {
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
        if (appendParamMap == null) {
            appendParamMap = new HashMap<>();
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
                        StringFilter relatedIdFilter = new StringFilter();
                        relatedIdFilter.setEquals(dto.getId());
                        Field relatedIdField = FieldUtils.getField(subCriteriaClass, relationDTO.getToName() + "Id", true);
                        relatedIdField.setAccessible(true);
                        relatedIdField.set(subCriteria, relatedIdFilter);
                        // 前面已经验证过权限了，以后不用再次验证权限
                        subCriteria.setAuthorityPass(Constants.yesNo.YES.getValue());
                        // 设置级联查询
                        subCriteria.setAssociationNameList(subAssociationNameList);
                        // 设置查询列
                        subCriteria.setSqlColumnList(criteria.getSqlColumnList());
                        // 调用级联的Service的方法进行查询
                        Object subDTOList = GlobalCache.getServiceMap().get(relationDTO.getToType())
                                .baseFindAllEntityPath(relationDTO.getToType(), subCriteria,
                                        entityPath + relationDTO.getFromName() + ".", appendParamMap).getData();
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
                        if (relatedId == null || "".equals(relatedId.toString().trim())) {
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
                        // 前面已经验证过权限了，以后不用再次验证权限
                        subCriteria.setAuthorityPass(Constants.yesNo.YES.getValue());
                        // 设置级联查询
                        subCriteria.setAssociationNameList(subAssociationNameList);
                        // 设置查询列
                        subCriteria.setSqlColumnList(criteria.getSqlColumnList());
                        // 调用级联的Service的方法进行查询
                        ReturnCommonDTO<O> subDTORtn = GlobalCache.getServiceMap().get(relationDTO.getFromType())
                                .baseFindOneEntityPath(relationDTO.getFromType(), (String)relatedId, subCriteria,
                                        entityPath + relationDTO.getToName() + ".", appendParamMap);
                        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(subDTORtn.getResultCode())) {
                            throw new CommonAlertException(subDTORtn.getResultCode(), subDTORtn.getErrMsg());
                        }
                        Object subDTO = subDTORtn.getData();
                        // 最终设置到主体dto的成员变量中
                        Field dtoField = FieldUtils.getField(dto.getClass(), relationDTO.getToName(), true);
                        dtoField.set(dto, subDTO);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if ("insertUser".equals(associationName)) {
                // 获取创建者
                String insertUserId = dto.getInsertUserId();
                if (insertUserId == null) {
                    continue;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationNameIter : associationNameList) {
                    if (associationNameIter.startsWith("insertUser.")) {
                        String associationName2 = associationNameIter.substring("insertUser.".length());
                        associationName2List.add(associationName2);
                    }
                }
                SystemUserCriteria insertUserCriteria = new SystemUserCriteria();
                // 此处不用再验证权限
                insertUserCriteria.setAuthorityPass(Constants.yesNo.YES.getValue());
                // 设置级联查询
                insertUserCriteria.setAssociationNameList(associationName2List);
                // 设置查询列
                insertUserCriteria.setSqlColumnList(criteria.getSqlColumnList());
                ReturnCommonDTO<SystemUserDTO> insertUserRtn = GlobalCache.getServiceMap().get("SystemUser")
                        .baseFindOneEntityPath("SystemUser", insertUserId, insertUserCriteria,
                                entityPath + "insertUser.", appendParamMap);
                dto.setInsertUser(insertUserRtn.getData());
            }
            if ("operateUser".equals(associationName)) {
                // 获取最后更新者
                String operateUserId = dto.getOperateUserId();
                if (operateUserId == null) {
                    continue;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationNameIter : associationNameList) {
                    if (associationNameIter.startsWith("operateUser.")) {
                        String associationName2 = associationNameIter.substring("operateUser.".length());
                        associationName2List.add(associationName2);
                    }
                }
                SystemUserCriteria operateUserCriteria = new SystemUserCriteria();
                // 此处不用再验证权限
                operateUserCriteria.setAuthorityPass(Constants.yesNo.YES.getValue());
                // 设置级联查询
                operateUserCriteria.setAssociationNameList(associationName2List);
                // 设置查询列
                operateUserCriteria.setSqlColumnList(criteria.getSqlColumnList());
                ReturnCommonDTO<SystemUserDTO> operateUserRtn = GlobalCache.getServiceMap().get("SystemUser")
                        .baseFindOneEntityPath("SystemUser", operateUserId, operateUserCriteria,
                                entityPath + "operateUser.", appendParamMap);
                dto.setOperateUser(operateUserRtn.getData());
            }
        }
        return dto;
    }

    /**
     * 新增或修改
     * @param entityTypeName 实体类型名
     * @param dto 主实体
     * @param appendMap 附加的传递参数
     * @return
     */
    default ReturnCommonDTO baseSave(String entityTypeName, O dto, Map<String, Object> appendMap) {
        if (appendMap == null) {
            appendMap = new HashMap<>();
        }
        ReturnCommonDTO result = ((BaseService)AopContext.currentProxy()).baseSaveInTrans(entityTypeName, dto, appendMap);
        baseDoAfterSaveOutTrans(dto, appendMap);
        return result;
    }

    /**
     * 新增或修改（事务操作）
     * @param entityTypeName 实体类型名
     * @param dto 主实体
     * @param appendMap 附加的传递参数
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO baseSaveInTrans(String entityTypeName, O dto, Map<String, Object> appendMap) {
        // 获取主键ID，根据ID存在与否判断是新增还是修改
        String dtoIdUpdate = dto.getId();
        // 设置当前用户和时间
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String nowUserId = GlobalCache.getCommonUserService().getCurrentUserId();
        dto.setInsertTime(dto.getId() == null ? nowTime : null);
        dto.setInsertUserId(dto.getId() == null ? nowUserId : null);
        dto.setUpdateTime(nowTime);
        dto.setOperateUserId(nowUserId);
        // 附加参数设置
        if (appendMap == null) {
            appendMap = new HashMap<>();
        }
        // 新增修改验证
        boolean continueSave = ((BaseService)AopContext.currentProxy()).baseSaveValidator(dto, appendMap);
        if (!continueSave) {
            // 不继续保存,在此处终止
            return new ReturnCommonDTO();
        }
        // 获取实体配置
        Class<? extends BaseDomain> entityClass = GlobalCache.getDomainClassMap().get(entityTypeName);
        T entity = null;
        try {
            entity = (T)entityClass.newInstance();
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        MyBeanUtil.copyNonNullProperties(dto, entity);
        // 新增或更新当前实体
        boolean result = saveOrUpdate(entity);
        // 新增或修改后的主键ID
        String dtoId = entity.getId();
        dto.setId(dtoId);
        // 新增或更新之后的操作
        ((BaseService)AopContext.currentProxy()).baseDoAfterSave(dto, appendMap);
        // 级联新增或修改
        List<BaseEntityConfigRelationDTO> relationList = GlobalCache.getEntityRelationsMap().get(entityTypeName);
        if (relationList == null || relationList.size() == 0) {
            return new ReturnCommonDTO();
        }
        for (BaseEntityConfigRelationDTO relationDTO : relationList) {
            if (!"from".equals(relationDTO.getFromOrTo())
                    || !"OneToMany".equals(relationDTO.getRelationType())) {
                // 只级联保存OneToMany类型的List（下级）的内容
                continue;
            }
            try {
                // 获取一对多的子关联属性（List）
                Field subListField = FieldUtils.getField(dto.getClass(), relationDTO.getFromName() + "List", true);
                Object subListObj = subListField.get(dto);
                if (subListObj == null) {
                    // 如果属性值为空，则跳过，不处理
                    continue;
                }
                List<? extends BaseDTO> subList = (List<? extends BaseDTO>) subListObj;
                if (dtoIdUpdate != null) {
                    // 如果是修改：获取传入的子属性的ID列表（去掉null的）
                    List<String> subDtoIdList = subList.stream()
                            .filter(subDTO -> subDTO.getId() != null)
                            .map(subDTO -> subDTO.getId()).collect(Collectors.toList());
                    // 数据库表的关联列字段名。注意：这里先限制为不能随意修改关联字段的数据库字段名，留待以后优化
                    String relatedColumnName = MyStringUtil.camelToUnderline(relationDTO.getToName()) + "_id";
                    if (subDtoIdList == null) {
                        // 如果子属性列表的所有都没有填写ID，则认为是全刷新，清空子属性列表
                        GlobalCache.getServiceMap().get(relationDTO.getToType()).baseDeleteByMapCascade(
                                relationDTO.getToType(),
                                new HashMap<String, Object>() {{put(relatedColumnName, dtoIdUpdate);}},
                                appendMap);
                    } else {
                        // 如果子属性列表的部分或全部填写了ID，则删除当前条件（指定了关联字段的值）下的其它数据
                        GlobalCache.getServiceMap().get(relationDTO.getToType()).baseDeleteByRelationIdWithoutIdList(
                                relationDTO.getToType(), relatedColumnName, dtoIdUpdate, subDtoIdList, appendMap);
                    }
                }
                // 然后，新增或修改子属性（需要级联保存的属性）
                subList.forEach(subDTO -> {
                    Field relationIdField = FieldUtils.getField(subDTO.getClass(), relationDTO.getToName() + "Id", true);
                    try {
                        relationIdField.set(subDTO, dtoId);
                    } catch (Exception e) {
                        throw new CommonException(e.getMessage());
                    }
                    if (subDTO.getId() == null) {
                        // 新增：设置创建人和创建时间
                        subDTO.setInsertUserId(nowUserId);
                        subDTO.setInsertTime(nowTime);
                    }
                    // 新增或修改：设置最新修改人和最新修改时间
                    subDTO.setOperateUserId(nowUserId);
                    subDTO.setUpdateTime(nowTime);
                    // 设置级联保存中
                    subDTO.setCascadeSave(Constants.yesNo.YES.getValue());
                    // 级联保存
                    GlobalCache.getServiceMap().get(relationDTO.getToType()).baseSave(relationDTO.getToType(), subDTO, new HashMap<>());
                });
            } catch (Exception e) {
                throw new CommonException(e.getMessage());
            }
        }
        // 所有保存完成后的操作
        ((BaseService)AopContext.currentProxy()).baseDoBeforeSaveReturn(dto, appendMap);
        return result ? new ReturnCommonDTO(Constants.commonReturnStatus.SUCCESS.getValue(), null, dtoId)
                : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据
     * @param entityTypeName 实体类型名
     * @param id 主键ID
     * @param appendMap 附加的传递参数
     * @return
     */
    default ReturnCommonDTO baseDeleteById(String entityTypeName, String id, Map<String, Object> appendMap) {
        if (appendMap == null) {
            appendMap = new HashMap<>();
        }
        ReturnCommonDTO result = ((BaseService)AopContext.currentProxy()).baseDeleteByIdTrans(entityTypeName, id, appendMap);
        baseDoAfterDeleteByIdOutTrans(id, appendMap);
        return result;
    }

    /**
     * 根据ID删除数据（事务操作，同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param entityTypeName 实体类型名
     * @param id 主键ID
     * @param appendMap 附加的传递参数
     * @return 结果返回码和消息
     * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事务的一致性
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO baseDeleteByIdTrans(String entityTypeName, String id, Map<String, Object> appendMap) {
        if (appendMap == null) {
            appendMap = new HashMap<>();
        }
        return ((BaseService)AopContext.currentProxy()).baseDeleteByMapCascade(
                entityTypeName, new HashMap<String, Object>() {{put("id", id);}}, appendMap);
    }

    /**
     * 根据ID列表删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param entityTypeName 实体类型名
     * @param idList 主键ID列表
     * @param appendMap 附加的传递参数
     * @return 结果返回码和消息
     * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO baseDeleteByIdList(String entityTypeName, List<String> idList, Map<String, Object> appendMap) {
        if (appendMap == null) {
            appendMap = new HashMap<>();
        }
        for (String id : idList) {
            ReturnCommonDTO returnCommonDTO = ((BaseService)AopContext.currentProxy()).baseDeleteByMapCascade(
                    entityTypeName, new HashMap<String, Object>() {{put("id", id);}}, appendMap);
            if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
                throw new CommonAlertException(returnCommonDTO.getErrMsg());
            }
        }
        return new ReturnCommonDTO();
    }

    /**
     * 根据指定条件删除数据（级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param entityTypeName 实体类型名
     * @param columnMap 表字段map对象
     * @param appendMap 附加的传递参数
     * @return 结果返回码和消息
     * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事务的一致性
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO baseDeleteByMapCascade(String entityTypeName, Map<String, Object> columnMap,
                                                   Map<String, Object> appendMap) {
        // 删除级联实体或置空关联字段或禁止删除
        listByMap(columnMap).forEach(domain -> {
            // 删除验证（例如权限验证等）
            ((BaseService)AopContext.currentProxy()).baseDeleteValidator(domain, appendMap);
            Optional.ofNullable(GlobalCache.getEntityRelationsMap().get(entityTypeName)).get().forEach(relationDTO -> {
                if ("SystemDictionary".equals(entityTypeName)) {
                    // 数据字典需要判断是否有使用该数据字典的信息
                    for (Map.Entry<String, List<BaseEntityConfigDicDTO>> entityDicEntry : GlobalCache.getEntityDicNameMap().entrySet()) {
                        String entityDicTypeName = entityDicEntry.getKey();
                        List<BaseEntityConfigDicDTO> entityConfigDicList = entityDicEntry.getValue();
                        for (BaseEntityConfigDicDTO entityConfigDicDTO : entityConfigDicList) {
                            if (entityConfigDicDTO.getDicType().equals(((SystemDictionary) domain).getDicType())) {
                                int useCount = GlobalCache.getMapperMap().get(entityDicTypeName).selectCount(
                                        new QueryWrapper<>().eq(entityConfigDicDTO.getColumnName(),
                                                ((SystemDictionary) domain).getDicCode()));
                                if (useCount > 0) {
                                    throw new CommonAlertException("有使用该数据字典的" + entityConfigDicDTO.getDicTypeName() + "信息，禁止删除。");
                                }
                            }
                        }
                    }
                }
                // 数据库表的关联列字段名。注意：这里先限制为不能随意修改关联字段的数据库字段名，留待以后优化
                String relatedColumnName = MyStringUtil.camelToUnderline(relationDTO.getToName()) + "_id";
                if ("from".equals(relationDTO.getFromOrTo())) {
                    // 级联删除是本体作为主，所以此处是from
                    if (Constants.cascadeDeleteType.FORBIDDEN.getValue().equals(relationDTO.getCascadeDelete())) {
                        // 级联禁止删除
                        int subCount = GlobalCache.getMapperMap().get(relationDTO.getToType()).selectCount(
                                new QueryWrapper<>().eq(relatedColumnName, domain.getId()));
                        if (subCount > 0) {
                            throw new CommonAlertException("有存在的" + relationDTO.getFromToComment() + "，禁止删除。");
                        }
                    } else if (Constants.cascadeDeleteType.NULL.getValue().equals(relationDTO.getCascadeDelete())) {
                        // 级联置空
                        GlobalCache.getMapperMap().get(relationDTO.getToType()).cascadeToNull(
                                GlobalCache.getEntityConfigMap().get(relationDTO.getToType()).getTableName(),
                                relatedColumnName, domain.getId());
                    } else {
                        // 默认：级联删除
                        GlobalCache.getServiceMap().get(relationDTO.getToType()).baseDeleteByMapCascade(
                                relationDTO.getToType(), new HashMap<String, Object>() {{
                                    put(relatedColumnName, domain.getId());
                                }}, appendMap);
                    }
                }
            });
        });
        // 根据指定条件删除当前实体的数据
        removeByMap(columnMap);
        return new ReturnCommonDTO();
    }

    /**
     * 查询单条数据
     * @param entityTypeName 实体类型名
     * @param id 主键ID
     * @param criteria 附加条件
     * @param appendParamMap 附加参数
     * @return 单条数据内容
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO<O> baseFindOne(String entityTypeName, String id, C criteria, Map<String, Object> appendParamMap) {
        return baseFindOneEntityPath(entityTypeName, id, criteria, null, appendParamMap);
    }

    /**
     * 分页查询
     * @param entityTypeName 实体类型名
     * @param criteria 查询条件
     * @param pageable 分页条件
     * @param appendParamMap 附加参数
     * @return 分页列表
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO<IPage<O>> baseFindPage(String entityTypeName, C criteria, MbpPage pageable,
                                                   Map<String, Object> appendParamMap) {
        return baseFindPageEntityPath(entityTypeName, criteria, pageable, null, appendParamMap);
    }

    /**
     * 查询所有
     * @param entityTypeName 实体类型名
     * @param criteria 查询条件
     * @param appendParamMap 附加参数
     * @return 数据列表
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO<List<O>> baseFindAll(String entityTypeName, C criteria, Map<String, Object> appendParamMap) {
        return baseFindAllEntityPath(entityTypeName, criteria, null, appendParamMap);
    }

    /**
     * 查询单条数据
     * @param entityTypeName 实体类型名
     * @param id 主键ID
     * @param criteria 附加条件
     * @param entityPath 实体的上层路径
     * @param appendParamMap 附加参数
     * @return 单条数据内容
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO<O> baseFindOneEntityPath(String entityTypeName, String id, C criteria, String entityPath,
                                                     Map<String, Object> appendParamMap) {
        if (appendParamMap == null) {
            appendParamMap = new HashMap<>();
        }
        if (entityPath == null) {
            entityPath = "";
        }
        // ID条件设定
        Wrapper<T> wrapper = baseIdEqualsPrepare(entityTypeName, id, criteria, appendParamMap);
        // 数据权限过滤
        ReturnCommonDTO interceptReturnInfo = new ReturnCommonDTO();   // 拦截的返回信息
        interceptReturnInfo.setDataType(Constants.returnDataType.OBJECT.getValue());  // 设置返回类型为object
        interceptReturnInfo.setResultCode(null);        // 初始化数据
        boolean dataFilterPass = false;
        try {
            dataFilterPass = ((BaseService) AopContext.currentProxy()).baseDataAuthorityFilter(
                    criteria, appendParamMap, interceptReturnInfo);
        } catch (Exception e) {
            getLog().error(e.getMessage(), e);
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "权限验证异常");
        }
        if (!dataFilterPass) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "没有该查询权限");
        }
        if (interceptReturnInfo.getResultCode() != null) {
            return interceptReturnInfo;
        }
        // 执行查询并返回结果
        Map<String, Object> appendParamMapFinal = appendParamMap;
        String entityPathFinal = entityPath;
        return Optional.ofNullable(baseFindOneSimple(wrapper, criteria, entityPath)).map(entity ->
                new ReturnCommonDTO(((BaseService)AopContext.currentProxy()).baseDoConvert(
                        entityTypeName, entity, criteria, entityPathFinal, appendParamMapFinal)))
                .orElse(new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "没有该数据"));
    }

    /**
     * 查询单条数据
     * @param queryWrapper 查询条件
     * @param criteria 附加条件
     * @param entityPath 实体的上层路径
     * @return
     */
    default T baseFindOneSimple(Wrapper<T> queryWrapper, C criteria, String entityPath) {
        // 最终查询的列，存放的内容是数据库中表的列名
        List<String> finalQueryColumnList = baseGetFinalQueryColumnList(criteria, entityPath);
        if (finalQueryColumnList.size() == 0) {
            // 如果不设置，默认查询全部列
            return getOne(queryWrapper);
        } else {
            // 主键id必须存在
            if (!finalQueryColumnList.contains("id")) {
                finalQueryColumnList.add(0, "id");
            }
            String[] finalQueryColumnArr = new String[finalQueryColumnList.size()];
            finalQueryColumnList.toArray(finalQueryColumnArr);
            return getOne(((QueryWrapper<T>) queryWrapper).select(finalQueryColumnArr));
        }
    }

    /**
     * 查询所有
     * @param entityTypeName 实体类型名
     * @param criteria 查询条件
     * @param entityPath 实体的上层路径
     * @param appendParamMap 附加参数
     * @return 数据列表
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO<List<O>> baseFindAllEntityPath(String entityTypeName, C criteria, String entityPath,
                                                           Map<String, Object> appendParamMap) {
        if (appendParamMap == null) {
            appendParamMap = new HashMap<>();
        }
        if (entityPath == null) {
            entityPath = "";
        }
        // 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
        Map<String, String> tableIndexMap = new HashMap<>();
        // 数据权限过滤
        ReturnCommonDTO interceptReturnInfo = new ReturnCommonDTO();   // 拦截的返回信息
        interceptReturnInfo.setDataType(Constants.returnDataType.LIST.getValue());  // 设置返回类型为list
        interceptReturnInfo.setResultCode(null);        // 初始化数据
        boolean dataFilterPass = ((BaseService)AopContext.currentProxy()).baseDataAuthorityFilter(
                criteria, appendParamMap, interceptReturnInfo);
        if (!dataFilterPass) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "没有该查询权限");
        }
        if (interceptReturnInfo.getResultCode() != null) {
            return interceptReturnInfo;
        }
        // 预处理orderBy的内容
        basePreOrderBy(criteria, tableIndexMap);
        // 获取查询SQL（select和join）
        String dataQuerySql = baseGetDataQuerySql(entityTypeName, criteria, tableIndexMap, entityPath);
        // 处理where条件
        Wrapper<T> wrapper = baseGetWrapper(entityTypeName, null, criteria, appendParamMap, null, tableIndexMap, null);
        // 执行查询并返回结果
        Map<String, Object> appendParamMapFinal = appendParamMap;
        String entityPathFinal = entityPath;
        return new ReturnCommonDTO(GlobalCache.getMapperMap().get(entityTypeName)
                .joinSelectList(dataQuerySql, wrapper, criteria.getLimit()).stream()
                .map(entity -> ((BaseService)AopContext.currentProxy()).baseDoConvert(
                        entityTypeName, (T)entity, criteria, entityPathFinal, appendParamMapFinal))
                .collect(Collectors.toList()));
    }

    /**
     * 分页查询
     * @param entityTypeName 实体类型名
     * @param criteria 查询条件
     * @param pageable 分页条件
     * @param entityPath 实体的上层路径
     * @param appendParamMap 附加参数
     * @return 分页列表
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO<IPage<O>> baseFindPageEntityPath(String entityTypeName, C criteria, MbpPage pageable,
                                                             String entityPath, Map<String, Object> appendParamMap) {
        if (appendParamMap == null) {
            appendParamMap = new HashMap<>();
        }
        if (entityPath == null) {
            entityPath = "";
        }
        Page<T> pageQuery = new Page<>(pageable.getPage(), pageable.getSize());
        // 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
        Map<String, String> tableIndexMap = new HashMap<>();
        // 数据权限过滤
        ReturnCommonDTO interceptReturnInfo = new ReturnCommonDTO();   // 拦截的返回信息
        interceptReturnInfo.setDataType(Constants.returnDataType.PAGE.getValue());  // 设置返回类型为page
        interceptReturnInfo.setResultCode(null);        // 初始化数据
        boolean dataFilterPass = ((BaseService)AopContext.currentProxy()).baseDataAuthorityFilter(
                criteria, appendParamMap, interceptReturnInfo);
        if (!dataFilterPass) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "没有该查询权限");
        }
        if (interceptReturnInfo.getResultCode() != null) {
            return interceptReturnInfo;
        }
        // 预处理orderBy的内容
        basePreOrderBy(criteria, tableIndexMap);
        // 获取查询SQL（select和join）
        String dataQuerySql = baseGetDataQuerySql(entityTypeName, criteria, tableIndexMap, entityPath);
        // 处理where条件
        String countQuerySql = baseGetCountQuerySql(entityTypeName, criteria, tableIndexMap);
        Wrapper<T> wrapper = baseGetWrapper(entityTypeName, null, criteria, appendParamMap, null, tableIndexMap, null);
        // 执行查询并返回结果
        Map<String, Object> appendParamMapFinal = appendParamMap;
        String entityPathFinal = entityPath;
        // 查数量
        int totalCount = GlobalCache.getMapperMap().get(entityTypeName).joinSelectCount(countQuerySql, wrapper);
        IPage<O> pageResult = null;
        if (totalCount == 0) {
            // count为0就没必要再去查一遍具体数据了
            pageResult = new IPage<O>() {
                @Override
                public List<O> getRecords() {
                    return new ArrayList<>();
                }
                @Override
                public IPage<O> setRecords(List<O> records) {
                    return null;
                }
                @Override
                public long getTotal() {
                    return 0;
                }
                @Override
                public IPage<O> setTotal(long total) {
                    return null;
                }
                @Override
                public long getSize() {
                    return pageable.getSize();
                }
                @Override
                public IPage<O> setSize(long size) {
                    return null;
                }
                @Override
                public long getCurrent() {
                    return pageable.getPage();
                }
                @Override
                public IPage<O> setCurrent(long current) {
                    return null;
                }
            };
        } else {
            // 查分页
            pageResult = GlobalCache.getMapperMap().get(entityTypeName).joinSelectPage(
                    pageQuery, dataQuerySql, wrapper)
                    .convert(entity -> ((BaseService) AopContext.currentProxy()).baseDoConvert(
                            entityTypeName, (T) entity, criteria, entityPathFinal, appendParamMapFinal));
        }
        pageResult.setTotal((long)totalCount);
        return new ReturnCommonDTO(pageResult);
    }

    /**
     * 查询个数
     * @param entityTypeName 实体类型名
     * @param criteria 查询条件
     * @return 个数
     */
    @Transactional(rollbackFor = Exception.class)
    default ReturnCommonDTO<Integer> baseFindCount(String entityTypeName, C criteria,
                                                   Map<String, Object> appendParamMap) {
        if (appendParamMap == null) {
            appendParamMap = new HashMap<>();
        }
        // 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
        Map<String, String> tableIndexMap = new HashMap<>();
        // 数据权限过滤
        ReturnCommonDTO interceptReturnInfo = new ReturnCommonDTO();   // 拦截的返回信息
        interceptReturnInfo.setDataType(Constants.returnDataType.INTEGER.getValue());  // 设置返回类型为integer
        interceptReturnInfo.setResultCode(null);        // 初始化数据
        boolean dataFilterPass = ((BaseService)AopContext.currentProxy()).baseDataAuthorityFilter(
                criteria, appendParamMap, interceptReturnInfo);
        if (!dataFilterPass) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "没有该查询权限");
        }
        if (interceptReturnInfo.getResultCode() != null) {
            return interceptReturnInfo;
        }
        // 获取查询SQL（select和join）
        String countQuerySql = baseGetCountQuerySql(entityTypeName, criteria, tableIndexMap);
        // 处理where条件
        Wrapper<T> wrapper = baseGetWrapper(entityTypeName, null, criteria, appendParamMap, null, tableIndexMap, null);
        // 执行查询并返回结果
        return new ReturnCommonDTO(GlobalCache.getMapperMap().get(entityTypeName).joinSelectCount(countQuerySql, wrapper));
    }

    /**
     * 处理Domain到DTO的转换
     * @param entityTypeName 实体类型名
     * @param entity 原始Entity实体
     * @param criteria 查询条件
     * @param entityPath 实体的上层路径
     * @param appendParamMap 附加的查询参数条件
     * @return 转换后的DTO
     */
    @Transactional(rollbackFor = Exception.class)
    default O baseDoConvert(String entityTypeName, T entity, C criteria, String entityPath,
                            Map<String, Object> appendParamMap) {
        if (appendParamMap == null) {
            appendParamMap = new HashMap<>();
        }
        // 获取实体配置
        Class<? extends BaseDTO> dtoClass = GlobalCache.getDtoClassMap().get(entityTypeName);
        O dto = null;
        try {
            dto = (O)dtoClass.newInstance();
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        MyBeanUtil.copyNonNullProperties(entity, dto);
        ((BaseService)AopContext.currentProxy()).baseGetAssociationsAll(entityTypeName, dto, criteria,
                entityPath, appendParamMap);
        return dto;
    }

    /**
     * 获取关联属性
     * @param entityTypeName 实体类型名
     * @param dto 主实体
     * @param criteria 关联属性的条件
     * @param entityPath 实体的上层路径
     * @param appendParamMap 附加的查询参数条件
     * @return 带关联属性的主实体
     */
    @Transactional(rollbackFor = Exception.class)
    default O baseGetAssociationsAll(String entityTypeName, O dto, C criteria, String entityPath,
                                     Map<String, Object> appendParamMap) {
        if (dto.getId() == null) {
            return dto;
        }
        if (appendParamMap == null) {
            appendParamMap = new HashMap<>();
        }
        // 处理关联属性（自定义）
        ((BaseService)AopContext.currentProxy()).baseGetAssociationsPrev(dto, criteria, appendParamMap);
        // 处理关联属性（共通）
        ((BaseService)AopContext.currentProxy()).baseGetAssociations(entityTypeName, dto, criteria, entityPath, appendParamMap);
        // 处理关联属性（自定义）
        ((BaseService)AopContext.currentProxy()).baseGetAssociationsNext(dto, criteria, appendParamMap);
        // 返回数据
        return dto;
    }

}