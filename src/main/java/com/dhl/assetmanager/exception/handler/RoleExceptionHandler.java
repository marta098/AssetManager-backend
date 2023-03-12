package com.dhl.assetmanager.exception.handler;

import com.dhl.assetmanager.dto.response.ExceptionDto;
import com.dhl.assetmanager.exception.RoleNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
class RoleExceptionHandler extends BaseExceptionHandler {

    public RoleExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    ResponseEntity<ExceptionDto> onRoleNotFoundException(RoleNotFoundException exception, Locale locale) {
        if (exception.isProvidedByUser()) {
            return response(exception, BAD_REQUEST, locale);
        }
        return response(exception, INTERNAL_SERVER_ERROR, locale);
    }

}
