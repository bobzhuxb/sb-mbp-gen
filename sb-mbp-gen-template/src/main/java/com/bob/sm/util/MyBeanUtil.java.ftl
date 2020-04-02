package ${packageName}.util;

import cn.hutool.core.util.ReflectUtil;
import ${packageName}.annotation.RestClassAllow;
import ${packageName}.annotation.RestFieldAllow;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean操作工具类
 * @author Bob
 */
public class MyBeanUtil {

    /**
     * 拷贝对象的非空属性
     * @param src
     * @param target
     */
    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 拷贝对象的非空属性
     * @param src
     * @param target
     * @param exceptPropertyNames 排除的属性（不拷贝）
     */
    public static void copyNonNullProperties(Object src, Object target, List<String> exceptPropertyNames) {
        List<String> notCopyPropertyNameList = Arrays.stream(getNullPropertyNames(src)).collect(Collectors.toList());
        for (String exceptPropertyName : exceptPropertyNames) {
            if (!notCopyPropertyNameList.contains(exceptPropertyName)) {
                notCopyPropertyNameList.add(exceptPropertyName);
            }
        }
        BeanUtils.copyProperties(src, target, notCopyPropertyNameList.stream().toArray(String[]::new));
    }

    /**
     * 获取对象的值非null的属性
     * @param source 对象实体
     * @paeam exceptNameList 排除在外的属性名List
     * @return
     */
    public static String[] getNotNullPropertyNames(Object source, List<String> exceptNameList) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> notEmptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            try {
                String propertyName = pd.getName();
                if ("class".equals(propertyName)) {
                    continue;
                }
                if (exceptNameList != null && exceptNameList.contains(propertyName)) {
                    continue;
                }
                Object srcValue = src.getPropertyValue(propertyName);
                if (srcValue != null) {
                    notEmptyNames.add(propertyName);
                }
            } catch (Exception e) {
                continue;
            }
        }
        String[] result = new String[notEmptyNames.size()];
        return notEmptyNames.toArray(result);
    }

    /**
     * 获取对象的值为null的属性
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            try {
                Object srcValue = src.getPropertyValue(pd.getName());
                if (srcValue == null) {
                    emptyNames.add(pd.getName());
                }
            } catch (Exception e) {
                continue;
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 根据注解名和属性设置属性值
     * @param annotationClass
     * @param defaultValue
     * @param object
     * @return
     */
    public static <T extends Annotation> Object setFieldValueByAnnotationNameAttr(Class<T> annotationClass, String attrName, Object defaultValue, Object object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                T annotation = field.getAnnotation(annotationClass);
                if (annotation != null) {
                    // 设置对象的访问权限，保证对private的属性的访问
                    field.setAccessible(true);
                    // 获取外部传入的值
                    Object value = field.get(object);
                    // 是否设置值
                    boolean forceSetValue = false;
                    if (value == null) {
                        // 没有传值，则设置为annotation指定的值或默认值
                        forceSetValue = true;
                    } else {
                        // 是否强制设置值
                        Method annotationMethodForce = annotation.annotationType().getDeclaredMethod("force");
                        Object force = null;
                        if (annotationMethodForce != null) {
                            force = annotationMethodForce.invoke(annotation);
                        }
                        if (force instanceof Boolean && (boolean) force) {
                            // 强制设置值，则忽略传入的值，直接设置为annotation指定的值或默认值
                            forceSetValue = true;
                        }
                    }
                    if (forceSetValue) {
                        // 没有传值，则设置为annotation指定的值或默认值
                        Method annotationMethod = annotation.annotationType().getDeclaredMethod(attrName);
                        if (annotationMethod != null) {
                            value = annotationMethod.invoke(annotation);
                        } else {
                            value = defaultValue;
                        }
                    }
                    switch (field.getGenericType().toString()) {
                        case "class java.lang.String":
                            field.set(object, value.toString());
                            break;
                        case "class java.lang.Integer":
                            field.set(object, Integer.parseInt(value.toString()));
                            break;
                        case "class java.lang.Double":
                            field.set(object, Double.parseDouble(value.toString()));
                            break;
                        case "class java.lang.Boolean":
                            field.set(object, new Boolean(value.toString()));
                            break;
                        default:
                            field.set(object, value);
                            break;
                    }
                }
            }
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据get/set许可屏蔽相关属性
     * @param toValue
     * @param object
     * @return
     */
    public static <T extends Annotation> Object setFieldValueByRestFieldAllow(String attrName, Object toValue, Object object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                RestFieldAllow annotation = field.getAnnotation(RestFieldAllow.class);
                if (annotation != null) {
                    Method annotationMethod = annotation.annotationType().getDeclaredMethod(attrName);
                    if (annotationMethod != null) {
                        Object annotationValue = annotationMethod.invoke(annotation);
                        if (annotationValue instanceof Boolean && !(Boolean)annotationValue) {
                            // 设置对象的访问权限，保证对private的属性的访问
                            field.setAccessible(true);
                            // 不允许访问或设置，设置为固定值
                            field.set(object, toValue);
                        }
                    }
                }
            }
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据类的get/set许可屏蔽所有属性
     * @param toValue
     * @param object
     * @return
     */
    public static <T extends Annotation> Object setAllFieldValue(String attrName, Object toValue, Object object) {
        // 关闭类的所有属性设置
        try {
            RestClassAllow annotation = object.getClass().getAnnotation(RestClassAllow.class);
            if (annotation != null) {
                Method annotationMethod = annotation.annotationType().getDeclaredMethod(attrName);
                if (annotationMethod != null) {
                    Object annotationValue = annotationMethod.invoke(annotation);
                    if (annotationValue instanceof Boolean && !(Boolean) annotationValue) {
                        Field[] fields = object.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            // 设置对象的访问权限，保证对private的属性的访问
                            field.setAccessible(true);
                            field.set(object, toValue);
                        }
                    }
                }
            }
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 所有字段置空
     * @param object
     * @return
     */
    public static void setAllFieldToNull(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                // 设置对象的访问权限，保证对private的属性的访问
                field.setAccessible(true);
                // 获取外部传入的值
                field.set(object, null);
            }
        } catch (Exception e) {
            // 异常不处理
        }
    }

    /**
     * 部分字段置空（反向）
     * @param object 数据实体
     * @param exceptFieldNameList 不置空的字段名
     * @return
     */
    public static void setAllFieldToNullExcept(Object object, List<String> exceptFieldNameList) {
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                String fieldName = field.getName();
                if (!exceptFieldNameList.contains(fieldName)) {
                    // 设置对象的访问权限，保证对private的属性的访问
                    field.setAccessible(true);
                    // 获取外部传入的值
                    field.set(object, null);
                }
            }
        } catch (Exception e) {
            // 异常不处理
        }
    }

    /**
     * 部分字段置空（正向）
     * @param object 数据实体
     * @param toNullFileNameList 置空的字段名
     * @return
     */
    public static void setSomeFieldToNull(Object object, List<String> toNullFileNameList) {
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                String fieldName = field.getName();
                if (toNullFileNameList.contains(fieldName)) {
                    // 设置对象的访问权限，保证对private的属性的访问
                    field.setAccessible(true);
                    // 获取外部传入的值
                    field.set(object, null);
                }
            }
        } catch (Exception e) {
            // 异常不处理
        }
    }

    /**
     * 设置实体的属性
     * @param object 指定实体
     * @param propertyNameFull 要设置的属性名，以.分隔属性层级
     * @param value 要设置的最终属性值
     */
    public static void setObjectProperty(Object object, String propertyNameFull, Object value) throws RuntimeException {
        // 属性切割
        String[] propertyNameArr = propertyNameFull.split("\\.");
        // 属性迭代
        Object propIter = object;
        try {
            for (int i = 0; i < propertyNameArr.length; i++) {
                BeanWrapper beanWrapper = new BeanWrapperImpl(propIter);
                java.beans.PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
                for (java.beans.PropertyDescriptor pd : pds) {
                    // 属性名称验证，只留下指定的属性
                    String propertyName = pd.getName();
                    if (!propertyNameArr[i].equals(propertyName)) {
                        continue;
                    }
                    if (i < propertyNameArr.length - 1) {
                        // 前面的一连串属性
                        Object innerObj = beanWrapper.getPropertyValue(propertyName);
                        if (innerObj == null) {
                            // 如果为空，新建一个实例设置进对应属性中
                            Class wrappedClass = pd.getPropertyType();
                            innerObj = ReflectUtil.newInstance(wrappedClass);
                            beanWrapper.setPropertyValue(propertyName, innerObj);
                        }
                        // 继续迭代
                        propIter = innerObj;
                    } else {
                        // 最后一层的属性（要设置值的属性）
                        beanWrapper.setPropertyValue(propertyName, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
