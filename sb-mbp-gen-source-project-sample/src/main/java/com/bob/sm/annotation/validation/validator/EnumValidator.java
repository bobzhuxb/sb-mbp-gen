package com.bob.sm.annotation.validation.validator;

import com.bob.sm.annotation.validation.ValidEnum;
import com.bob.sm.config.Constants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EnumValidator implements ConstraintValidator<ValidEnum, Object> {

    Class<?>[] cls; //枚举类

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        cls = constraintAnnotation.target();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (cls.length > 0) {
            for (Class<?> cl : cls) {
                try {
                    if (cl.isEnum()) {
                        // 枚举类验证
                        Method method = cl.getMethod("values");
                        Constants.EnumInter enumInters[] = (Constants.EnumInter[]) method.invoke(null, null);
                        for (Constants.EnumInter enumInter : enumInters) {
                            Object enumValue = enumInter.getValue();
                            if (value.equals(enumValue)) {
                                return true;
                            }
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return true;
        }
        return false;
    }
}
