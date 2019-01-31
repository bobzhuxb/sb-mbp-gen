package com.bob.sm.annotation;

import java.lang.annotation.*;

/**
 * 对Rest请求关闭类的所有属性的设置或访问
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestClassAllow {

    // 默认允许设置
    boolean allowGet() default true;

    // 默认允许设置
    boolean allowSet() default true;

}
