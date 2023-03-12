package com.dhl.assetmanager.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class OneOfValidator implements ConstraintValidator<OneOf, String> {

    private String[] fields;

    @Override
    public void initialize(OneOf oneOf) {
        fields = oneOf.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(fields).contains(value);
    }
}
