package ${packageName}.annotation;

import java.lang.annotation.*;

/**
 * 注释
 * @author Bob
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GenComment {

    String value();

}
