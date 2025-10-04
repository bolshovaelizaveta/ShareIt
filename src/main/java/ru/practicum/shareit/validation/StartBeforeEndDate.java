package ru.practicum.shareit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndDateValidator.class)
public @interface StartBeforeEndDate {
    String message() default "Дата окончания бронирования должна быть после даты начала";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}