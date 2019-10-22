package ${packageName}.annotation.validation;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 最多两位小数（只能为正数）都验证通过
 * @author Bob
 */
@ConstraintComposition(CompositionType.OR)
@Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
@Null
@Length(min = 0, max = 0)
@Documented
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface Valid2BitDecimal {
    String message() default "数量校验错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
