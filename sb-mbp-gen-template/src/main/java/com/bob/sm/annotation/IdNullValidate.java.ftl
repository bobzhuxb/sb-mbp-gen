package ${packageName}.annotation;

import java.lang.annotation.*;

/**
 * id是否为空的验证
 * @author Bob
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdNullValidate {

    // 是否必须为空
    boolean mustNull() default false;

    // 是否必须为非空
    boolean mustNotNull() default false;

}
