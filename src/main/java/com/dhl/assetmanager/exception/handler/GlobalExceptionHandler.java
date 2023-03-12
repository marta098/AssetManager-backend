package com.dhl.assetmanager.exception.handler;

import com.dhl.assetmanager.dto.response.ExceptionDto;
import com.dhl.assetmanager.dto.response.ValidationExceptionDto;
import com.dhl.assetmanager.exception.TechnicalException;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
class GlobalExceptionHandler extends BaseExceptionHandler {

    public GlobalExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ExceptionDto> onUnexpectedException(RuntimeException exception, Locale locale) {
        exception.printStackTrace();
        return response(exception, INTERNAL_SERVER_ERROR, locale);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionDto> onValidationException(MethodArgumentNotValidException exception,
                                                                 Locale locale) {
        var validationErrors = getValidationErrors(exception);
        String description = getDescription(exception, locale);

        return ResponseEntity.badRequest().body(new ValidationExceptionDto(description, validationErrors));
    }

    @ExceptionHandler(TechnicalException.class)
    ResponseEntity<ExceptionDto> onTechnicalException(TechnicalException exception, Locale locale) {
        return response(exception, INTERNAL_SERVER_ERROR, locale);
    }

    private Map<String, String> getValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> validationErrors = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));
        return validationErrors;
    }


}
