package ${packageName}.annotation;

import java.lang.annotation.*;

/**
 * 在插入数据时，自动设置为某值
 * 注意：该annotation会自动忽略外部传入的字段值
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateInitValue {

    String value();

    boolean force();
}
