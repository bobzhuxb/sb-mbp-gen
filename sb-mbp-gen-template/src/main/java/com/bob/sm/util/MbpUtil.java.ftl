package ${packageName}.util;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${packageName}.dto.criteria.BaseCriteria;
import ${packageName}.dto.criteria.filter.*;
import ${packageName}.service.BaseService;
import ${packageName}.web.rest.errors.CommonException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.formula.functions.T;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MbpUtil {

    /**
     * 将criteria转换为wrapper
     * @param wrapper 转换前或转换后的wrapper
     * @param criteria 转换前的条件
     * @param clazz 查询结果的类
     * @param lastFieldName 最后的field名
     * @param tableIndexMap 表index的Map
     * @param baseService 用于增强条件查询的功能
     * @param <M>
     * @param <T>
     * @param <C>
     * @return 转换后的wrapper
     */
    public static <M, T, C> Wrapper<T> getWrapper(QueryWrapper<T> wrapper, C criteria, Class clazz,
            String lastFieldName, Map<String, String> tableIndexMap, BaseService<T> baseService) {
        if (wrapper == null) {
            wrapper = new QueryWrapper<>();
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
                    String orderDirection = "";
                    if (orderByDetail.length > 1) {
                        orderDirection = orderByDetail[1];
                    }
                    String orderFieldName = orderByDetail[0];
                    if (orderFieldName.contains(".")) {
                        orderFieldName = orderFieldName.substring(orderFieldName.lastIndexOf(".") + 1);
                    }
                    if (orderByDetail.length > 2 || "".equals(orderFieldName)
                            || (!"".equals(orderDirection) && !"asc".equalsIgnoreCase(orderDirection) && !"desc".equalsIgnoreCase(orderDirection))) {
                        continue;
                    }
                    boolean isAsc = !"desc".equalsIgnoreCase(orderDirection);
                    String orderColumnName = subTableName + "." + StringUtil.camelToUnderline(orderFieldName);
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
                    // join的实体
                    String typeName = result.getClass().getName();
                    String domainTypeName = typeName.substring(typeName.lastIndexOf(".") + 1, typeName.lastIndexOf("Criteria"));
                    Class entityClass = Class.forName("${packageName}.domain." + domainTypeName);
                    // 级联的域名
                    String nowFieldName = lastFieldName == null ? fieldName : lastFieldName + "." + fieldName;
                    wrapper = (QueryWrapper<T>)getWrapper(wrapper, result, entityClass, nowFieldName,
                            tableIndexMap, baseService);
                }
                if (!filterOrCriteria) {
                    // 附加的其它类型的查询条件
                    wrapper = (QueryWrapper<T>) baseService.wrapperEnhance(wrapper, tableName, fieldName, result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new CommonException(e.getMessage());
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
