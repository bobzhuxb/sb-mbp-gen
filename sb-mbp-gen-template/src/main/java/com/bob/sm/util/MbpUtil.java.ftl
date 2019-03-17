package ${packageName}.util;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${packageName}.domain.BaseDomain;
import ${packageName}.dto.criteria.BaseCriteria;
import ${packageName}.dto.criteria.filter.*;
import ${packageName}.dto.help.NormalCriteriaDTO;
import ${packageName}.service.BaseService;
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

public class MbpUtil {

    /**
     * 将criteria转换为wrapper
     * @param wrapper 转换前或转换后的wrapper
     * @param criteria 转换前的条件
     * @param clazz 查询结果的类
     * @param lastFieldName 最后的field名
     * @param tableIndexMap 表index的Map
     * @param baseService 用于增强条件查询的Service
     * @param normalCriteriaList 其他非框架的条件
     * @param <M>
     * @param <T>
     * @param <C>
     * @return 转换后的wrapper
     */
    public static <M, T extends BaseDomain, C extends BaseCriteria> Wrapper<T> getWrapper(
            QueryWrapper<T> wrapper, C criteria, Class clazz, String lastFieldName, Map<String, String> tableIndexMap,
            BaseService<T, C> baseService, List<NormalCriteriaDTO> normalCriteriaList) {
        // 是否调用的首栈（递归的第一次调用）
        boolean firstStackElement = false;
        if (wrapper == null) {
            firstStackElement = true;
            wrapper = new QueryWrapper<>();
            normalCriteriaList = new ArrayList<>();
        }
        // 获取表名字
        String tableName = ((TableName)clazz.getAnnotation(TableName.class)).value();
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
                    Class entityClass = Class.forName("${packageName}.domain." + domainTypeName);
                    // 级联的域名
                    String nowFieldName = lastFieldName == null ? fieldName : lastFieldName + "." + fieldName;
                    wrapper = (QueryWrapper<T>)getWrapper(wrapper, (C)result, entityClass, nowFieldName,
                            tableIndexMap, baseService, normalCriteriaList);
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
                wrapper = (QueryWrapper<T>) baseService.wrapperEnhance(wrapper, criteria, normalCriteriaList,
                        revertTableIndexMap);
            }
        }
        return wrapper;
    }

    /**
     * orderBy的字段自动先生成个空条件
     * @return
     */
    public static void preOrderBy(Object criteria, Map<String, String> tableIndexMap) {
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

    public static String getChangedTableName(String cascadeEntityAndField, Map<String, String> tableIndexMap)
            throws Exception {
        String changedTableName = null;
        String key = cascadeEntityAndField.substring(0, cascadeEntityAndField.lastIndexOf("."));
        String tableIndexAndName = tableIndexMap.get(key);
        if (tableIndexAndName != null) {
            // 已经加入过tableIndexMap
            String tableIndex = tableIndexAndName.split("_")[0];
            String domainTypeName = tableIndexAndName.split("_")[1];
            Class entityClass = Class.forName("${packageName}.domain." + domainTypeName);
            changedTableName = ((TableName)entityClass.getAnnotation(TableName.class)).value();
            changedTableName = changedTableName + "_" + tableIndex;
        }
        return changedTableName;
    }

    public static <T> IPage<T> selectPage(BaseMapper<T> baseMapper, IPage<T> pageQuery, Wrapper<T> wrapper) {
        IPage<T> resultPage = baseMapper.selectPage(pageQuery, wrapper);
        int totalCount = baseMapper.selectCount(wrapper);
        resultPage.setTotal((long)totalCount);
        return resultPage;
    }

}
