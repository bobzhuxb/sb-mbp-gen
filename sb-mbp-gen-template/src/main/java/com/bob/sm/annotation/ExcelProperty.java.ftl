package ${packageName}.annotation;

import java.lang.annotation.*;

/**
 * Excel解析
 * @author Bob
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelProperty {

    // 列标题
    String value();

    // 排序
    int index();

    // 验证正则
    String regex() default ".*";

    // 是否允许空值
    boolean nullable() default true;

    // 日期格式
    String dateFormat() default "yyyy-MM-dd";

    // 取值范围
    String[] optionList() default {};

}
