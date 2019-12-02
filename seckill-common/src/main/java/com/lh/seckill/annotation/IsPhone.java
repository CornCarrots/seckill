package com.lh.seckill.annotation;

import cn.hutool.core.util.StrUtil;
import com.lh.seckill.util.ValidatorUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IsPhone.IsMobileValidator.class}) // 这个注解的参数指定用于校验工作的是哪个类
public @interface IsPhone {
    /**
     * 如果校验不通过时的提示信息
     *
     * @return
     */
    String message() default "手机号码格式有误！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IsMobileValidator implements ConstraintValidator<IsPhone, String> {

        @Override
        public void initialize(IsPhone constraintAnnotation) {
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
            // 如果所检验字段可以为空
            return StrUtil.isEmptyIfStr(value) || ValidatorUtil.isMobile(value);
        }
    }
}
