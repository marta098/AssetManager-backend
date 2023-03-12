package com.dhl.assetmanager.exception.handler;

import com.dhl.assetmanager.dto.response.ExceptionDto;
import com.dhl.assetmanager.exception.UserAlreadyExistsException;
import com.dhl.assetmanager.exception.UserNotFoundException;
import com.dhl.assetmanager.exception.UserNotPermittedException;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
class AuthorizationExceptionHandler extends BaseExceptionHandler {

    public AuthorizationExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ExceptionDto> onBadCredentialsException(BadCredentialsException exception, Locale locale) {
        return response(exception, UNAUTHORIZED, locale);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<ExceptionDto> onBadCredentialsException(UserAlreadyExistsException exception, Locale locale) {
        return response(exception, CONFLICT, locale);
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ExceptionDto> onUserNotFoundException(UserNotFoundException exception, Locale locale) {
        return response(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ExceptionDto> onAccessDeniedException(AccessDeniedException exception, Locale locale) {
        return response(exception, FORBIDDEN, locale);
    }

    @ExceptionHandler(UserNotPermittedException.class)
    ResponseEntity<ExceptionDto> onUserNotPermittedException(UserNotPermittedException exception, Locale locale) {
        return response(exception, FORBIDDEN, locale);
    }

    @ExceptionHandler(DisabledException.class)
    ResponseEntity<ExceptionDto> onDisabledException(DisabledException exception, Locale locale) {
        return response(exception, UNAUTHORIZED, locale);
    }

}
