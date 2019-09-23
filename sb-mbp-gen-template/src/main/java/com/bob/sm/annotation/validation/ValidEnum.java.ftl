package ${packageName}.annotation.validation;

import ${packageName}.annotation.validation.validator.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 枚举范围验证，只允许枚举值范围内的数据
 * @author Bob
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValidator.class})
@Documented
public @interface ValidEnum {
    String message() default "传入值超出指定范围";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?>[] target() default {};
}
