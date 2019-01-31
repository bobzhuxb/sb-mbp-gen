package ${packageName}.annotation;

import java.lang.annotation.*;

/**
 * 对Rest请求关闭某些字段的设置或访问
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFieldAllow {

    // 默认允许获取
    boolean allowGet() default true;

    // 默认允许设置
    boolean allowSet() default true;

}
