package com.bob.sm.annotation;

import java.lang.annotation.*;

/**
 * 在插入数据时，自动设置为当前时间
 * 注意：该annotation会自动忽略外部传入的时间
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateTime {

    // 默认为当前时间
    String id() default "now";
}
