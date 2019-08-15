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
 * 经纬度验证，为空和正确的经纬度都验证通过，<br/>
 * 三位以内整数 + 9位以内的小数 （可为负数）
 *
 */
@ConstraintComposition(CompositionType.OR)
@Pattern(regexp = "^(\\-)?\\d{1,3}(\\.\\d{1,9})?$")
@Null
@Length(min = 0, max = 0)
@Documented
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface ValidLonLat {
    String message() default "身份证号校验错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}