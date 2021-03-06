package com.openclassrooms.payMyBuddy.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IbanValidator.class)


public @interface ValidIban {
    String message() default "Your IBAN code is not valid, please read information above to learn more.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
