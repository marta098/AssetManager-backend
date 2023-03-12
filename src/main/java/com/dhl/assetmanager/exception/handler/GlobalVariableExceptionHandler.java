package com.dhl.assetmanager.exception.handler;

import com.dhl.assetmanager.dto.response.ExceptionDto;
import com.dhl.assetmanager.exception.GlobalVariableNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class GlobalVariableExceptionHandler extends BaseExceptionHandler {

    public GlobalVariableExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }

    @ExceptionHandler(GlobalVariableNotFoundException.class)
    ResponseEntity<ExceptionDto> onGlobalVariableNotFoundException(GlobalVariableNotFoundException exception, Locale locale) {
        return response(exception, INTERNAL_SERVER_ERROR, locale);
    }

}
