package com.dhl.assetmanager.dto.response;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationExceptionDto extends ExceptionDto {

    private final Map<String, String> validationErrors;

    public ValidationExceptionDto(String description, Map<String, String> validationErrors) {
        super(description);
        this.validationErrors = validationErrors;
    }

}

