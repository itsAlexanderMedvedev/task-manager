package com.amedvedev.taskmanager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
public class PresentOrFutureDateValidator implements ConstraintValidator<PresentOrFutureDate, LocalDate> {

    @Override
    public void initialize(PresentOrFutureDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return !localDate.isBefore(LocalDate.now());
    }
}
