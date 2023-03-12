package com.dhl.assetmanager.exception.handler;

import com.dhl.assetmanager.dto.response.ExceptionDto;
import com.dhl.assetmanager.exception.ReportAlreadyGeneratedException;
import com.dhl.assetmanager.exception.ReportNotFoundException;
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
class ReportExceptionHandler extends BaseExceptionHandler {

    public ReportExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }

    @ExceptionHandler(ReportNotFoundException.class)
    ResponseEntity<ExceptionDto> onReportNotFoundException(ReportNotFoundException exception, Locale locale) {
        return response(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(ReportAlreadyGeneratedException.class)
    ResponseEntity<ExceptionDto> onReportAlreadyGeneratedException(ReportAlreadyGeneratedException exception, Locale locale) {
        return response(exception, BAD_REQUEST, locale);
    }


}
