package com.dhl.assetmanager.exception.handler;

import com.dhl.assetmanager.dto.response.ExceptionDto;
import com.dhl.assetmanager.exception.*;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
class OrderExceptionHandler extends BaseExceptionHandler {

    public OrderExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    ResponseEntity<ExceptionDto> onOrderNotFoundException(OrderNotFoundException exception, Locale locale) {
        return response(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(UserIsNotEmployeeItException.class)
    ResponseEntity<ExceptionDto> onUserIsNotEmployeeItException(UserIsNotEmployeeItException exception, Locale locale) {
        return response(exception, BAD_REQUEST, locale);
    }

    @ExceptionHandler(AssetModelNotFoundException.class)
    ResponseEntity<ExceptionDto> onAssetModelNotFoundException(AssetModelNotFoundException exception, Locale locale) {
        return response(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(MpkNumberNotFoundException.class)
    ResponseEntity<ExceptionDto> onMpkNumberNotFoundException(MpkNumberNotFoundException exception, Locale locale) {
        return response(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(PickupDateTooEarlyException.class)
    ResponseEntity<ExceptionDto> onPickupDateTooEarlyException(PickupDateTooEarlyException exception, Locale locale) {
        return response(exception, BAD_REQUEST, locale);
    }

    @ExceptionHandler(AssetAlreadyCreatedException.class)
    ResponseEntity<ExceptionDto> onAssetAlreadyCreatedException(AssetAlreadyCreatedException exception, Locale locale) {
        return response(exception, BAD_REQUEST, locale);
    }

}
