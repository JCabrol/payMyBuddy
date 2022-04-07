package com.openclassrooms.payMyBuddy.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.PasswordAuthentication;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Your password is not valid, please read information above to learn more.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

