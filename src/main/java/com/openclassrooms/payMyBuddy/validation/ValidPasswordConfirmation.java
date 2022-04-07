package com.openclassrooms.payMyBuddy.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= PasswordConfirmationValidator.class)
public @interface ValidPasswordConfirmation {
    String message() default "Password and confirmation should be the same.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

