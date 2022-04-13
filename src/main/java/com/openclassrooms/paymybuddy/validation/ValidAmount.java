package com.openclassrooms.paymybuddy.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= AmountValidator.class)
public @interface ValidAmount {
    String message() default "You don't have enough money to make this transaction";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

