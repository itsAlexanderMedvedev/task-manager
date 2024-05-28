package com.amedvedev.taskmanager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PresentOrFutureDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PresentOrFutureDate {
    String message() default "Date must be in the future or present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
