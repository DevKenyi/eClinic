package com.example.eclinic001.configuration.sercuityConfiguration.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = UserValidator.class)
    public @interface ValidUser {
        String message() default "Password mismatch";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

