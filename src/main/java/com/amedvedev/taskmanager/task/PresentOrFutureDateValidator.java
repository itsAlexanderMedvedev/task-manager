package com.amedvedev.taskmanager.task;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
public class PresentOrFutureDateValidator implements ConstraintValidator<PresentOrFutureDate, LocalDate> {

    @Override
    public void initialize(PresentOrFutureDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println("VALIDATION PERFORMED ON " + localDate);
        return !localDate.isBefore(LocalDate.now());
    }
}
