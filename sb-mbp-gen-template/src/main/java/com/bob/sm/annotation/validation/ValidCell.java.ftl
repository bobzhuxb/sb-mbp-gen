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
 * 手机号验证，为空和正确的手机号都验证通过，<br/>
 * 手机号为1开头的11位数字
 * @author Bob
 *
 */
@ConstraintComposition(CompositionType.OR)
@Pattern(regexp = "^1[0-9]{10}$")
@Null
@Length(min = 0, max = 0)
@Documented
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface ValidCell {
    String message() default "手机号校验错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
