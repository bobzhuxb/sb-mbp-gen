package ${packageName}.util;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import ${packageName}.dto.help.DynamicDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.beanutils.PropertyUtilsBean;


/**
 * 动态生成类的属性、并且赋值
 */
public class ReflectUtil {

    static Logger logger = LogManager.getLogger(ReflectUtil.class);

    @SuppressWarnings("rawtypes")
    public static Object getTarget(Object dest, Map<String, Object> addProperties) {
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(dest);
        Map<String, Class> propertyMap = new HashMap<>();
        for (PropertyDescriptor d : descriptors) {
            if (!"class".equalsIgnoreCase(d.getName())) {
                propertyMap.put(d.getName(), d.getPropertyType());
            }
        }
        // add extra properties
        addProperties.forEach((k, v) -> propertyMap.put(k, v.getClass()));
        // new dynamic bean
        DynamicDTO dynamicDTO = new DynamicDTO(dest.getClass(), propertyMap);
        // add old value
        propertyMap.forEach((k, v) -> {
            try {
                // filter extra properties
                if (!addProperties.containsKey(k)) {
                    dynamicDTO.setValue(k, propertyUtilsBean.getNestedProperty(dest, k));
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
        // add extra value
        addProperties.forEach((k, v) -> {
            try {
                dynamicDTO.setValue(k, v);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
        Object target = dynamicDTO.getTarget();
        return target;
    }

    public static void main(String[] args) {
//        ToBeChangedVo oldVo = new ToBeChangedVo();
//        Map<String, Object> addProperties = new HashMap<>();
//        addProperties.put("abc", "你好");
//        ToBeChangedVo newVo = (ToBeChangedVo) getTarget(oldVo, addProperties);
//        System.out.println(JSON.toJSONString(newVo));
    }
}
