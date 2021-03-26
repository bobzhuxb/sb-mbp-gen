package ${packageName}.util;

import com.ts.bpoi.annotation.ExcelProperty;

import java.util.List;

/**
 * Excel工具类
 * @author Bob
 */
public class ExcelUtil {

    /**
     * 修改指定字段的ExcelProperty注解的optionList值
     * @param objClass 指定类
     * @param fieldName 指定字段名
     * @param optionList 可选范围列表
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void changeOptionListOfExcelProperty(Class<?> objClass, String fieldName,
                                                       List<String> optionList) throws NoSuchFieldException, IllegalAccessException {
        String[] optionArray = new String[optionList.size()];
        optionList.toArray(optionArray);
        MyBeanUtil.changeStaticAnnotationOfField(objClass, fieldName, ExcelProperty.class, "optionList", optionArray);
    }

}
