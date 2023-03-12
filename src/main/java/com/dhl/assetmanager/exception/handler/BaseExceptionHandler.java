package com.dhl.assetmanager.exception.handler;

import com.dhl.assetmanager.dto.response.ExceptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

@RequiredArgsConstructor
class BaseExceptionHandler {

    private final MessageSource messageSource;

    protected ResponseEntity<ExceptionDto> response(Exception exception, HttpStatus status, Locale locale) {
        String description = getDescription(exception, locale);
        return ResponseEntity.status(status).body(new ExceptionDto(description));
    }

    protected String getDescription(Exception exception, Locale locale) {
        var key = exception.getClass().getSimpleName();
        String description;
        try {
            description = messageSource.getMessage(key, new Object[]{}, locale);
        } catch (NoSuchMessageException ex) {
            description = key;
        }
        return description;
    }

}