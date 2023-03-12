package com.dhl.assetmanager.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
@Constraint(validatedBy = EnumValuesValidator.class)
public @interface EnumValues {

    Class<? extends Enum<?>> enumClass();

    String message() default "Value is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}