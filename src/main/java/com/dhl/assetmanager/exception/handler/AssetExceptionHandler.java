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
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class AssetExceptionHandler extends BaseExceptionHandler {

    public AssetExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }

    @ExceptionHandler(AssetNotFoundException.class)
    ResponseEntity<ExceptionDto> onAssetNotFoundException(AssetNotFoundException exception, Locale locale) {
        return response(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(AssetIllegalArgumentException.class)
    ResponseEntity<ExceptionDto> onAssetIllegalArgumentException(AssetIllegalArgumentException exception, Locale locale) {
        return response(exception, BAD_REQUEST, locale);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    ResponseEntity<ExceptionDto> onLocationNotFoundException(LocationNotFoundException exception, Locale locale) {
        return response(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(StockroomNotFoundException.class)
    ResponseEntity<ExceptionDto> onLocationNotFoundException(StockroomNotFoundException exception, Locale locale) {
        return response(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(SerialNumberAlreadyExistsException.class)
    ResponseEntity<ExceptionDto> onSerialNumberAlreadyExistsException(SerialNumberAlreadyExistsException exception, Locale locale) {
        return response(exception, CONFLICT, locale);
    }

}
