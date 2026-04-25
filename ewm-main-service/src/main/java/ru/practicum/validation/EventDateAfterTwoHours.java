package ru.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventDateValidator.class)
public @interface EventDateAfterTwoHours {

    String message() default "Дата события должна быть не раньше, чем через 2 часа от времени создания";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}