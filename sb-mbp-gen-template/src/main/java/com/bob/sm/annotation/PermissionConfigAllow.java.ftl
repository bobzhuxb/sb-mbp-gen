package ${packageName}.annotation;

import java.lang.annotation.*;

/**
 * 是否允许配置该权限
 * @author Bob
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionConfigAllow {

    // 默认允许配置
    boolean allow() default true;
}
