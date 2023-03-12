package com.dhl.assetmanager.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnumValuesValidator implements ConstraintValidator<EnumValues, String> {

    List<String> values;

    @Override
    public void initialize(EnumValues constraintAnnotation) {
        values = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();

        Enum<?>[] enumValues = enumClass.getEnumConstants();

        Arrays.stream(enumValues).forEach(value -> values.add(value.toString().toUpperCase()));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return values.contains(value);
    }

}
