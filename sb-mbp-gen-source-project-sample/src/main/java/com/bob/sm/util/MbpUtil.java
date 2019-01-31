package com.bob.sm.util;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bob.sm.dto.SmMeetingGuestDTO;
import com.bob.sm.dto.criteria.BaseCriteria;
import com.bob.sm.dto.criteria.filter.*;
import com.bob.sm.mapper.BaseCommonMapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class MbpUtil {

    public static <T, C> Wrapper<T> getWrapper(QueryWrapper<T> wrapper, C criteria, Class clazz) {
        if (wrapper == null) {
            wrapper = new QueryWrapper<>();
        }
        // 获取表名字
        String tableName = ((TableName)clazz.getAnnotation(TableName.class)).value();
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
                    String[] orderByDetail = orderBy.trim().split("\\s");
                    String orderDirection = "";
                    if (orderByDetail.length > 1) {
                        orderDirection = orderByDetail[1];
                    }
                    String orderFieldName = orderByDetail[0];
                    if (orderByDetail.length > 2 || "".equals(orderFieldName)
                            || (!"".equals(orderDirection) && !"asc".equalsIgnoreCase(orderDirection) && !"desc".equalsIgnoreCase(orderDirection))) {
                        continue;
                    }
                    boolean isAsc = !"desc".equalsIgnoreCase(orderDirection);
                    String orderColumnName = tableName + "." + StringUtil.camelToUnderline(orderFieldName);
                    wrapper.orderBy(true, isAsc, orderColumnName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 其他条件
        Field[] fields = criteriaClazz.getDeclaredFields();
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
                    if (((RangeFilter)result).getGreaterThan() != null) {
                        wrapper.gt(columnName, ((RangeFilter)result).getGreaterThan());
                    }
                    if (((RangeFilter)result).getGreaterOrEqualThan() != null) {
                        wrapper.ge(columnName, ((RangeFilter)result).getGreaterOrEqualThan());
                    }
                    if (((RangeFilter)result).getLessThan() != null) {
                        wrapper.lt(columnName, ((RangeFilter)result).getLessThan());
                    }
                    if (((RangeFilter)result).getLessOrEqualThan() != null) {
                        wrapper.le(columnName, ((RangeFilter)result).getLessOrEqualThan());
                    }
                }
                if (result instanceof Filter) {
                    if (((Filter)result).getEquals() != null) {
                        wrapper.eq(columnName, ((Filter)result).getEquals());
                    }
                    if (((Filter)result).getIn() != null) {
                        wrapper.in(columnName, ((Filter)result).getIn());
                    }
                }
                if (result instanceof BaseCriteria) {
                    // join的实体
                    String typeName = result.getClass().getName();
                    String domainTypeName = typeName.substring(typeName.lastIndexOf(".") + 1, typeName.lastIndexOf("Criteria"));
                    Class entityClass = Class.forName("com.bob.sm.domain." + domainTypeName);
                    wrapper = (QueryWrapper<T>)getWrapper(wrapper, result, entityClass);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wrapper;
    }

    public static <T> IPage<T> selectPage(BaseMapper<T> baseMapper, IPage<T> pageQuery, Wrapper<T> wrapper) {
        IPage<T> resultPage = baseMapper.selectPage(pageQuery, wrapper);
        int totalCount = baseMapper.selectCount(wrapper);
        resultPage.setTotal((long)totalCount);
        return resultPage;
    }

}
