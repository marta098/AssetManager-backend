package com.dhl.assetmanager.logger;

import com.dhl.assetmanager.dto.request.UserCredentialsRequest;
import com.dhl.assetmanager.dto.request.UserRegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthenticationControllerLogger {

    @Before("execution(* com.dhl.assetmanager.controller.AuthenticationController.login(..)) && args(userCredentials)")
    void logLoggingIn(UserCredentialsRequest userCredentials) {
        log.info("User {} is logging in", userCredentials.getUsername());
    }

    @Before("execution(* com.dhl.assetmanager.controller.AuthenticationController.register(..)) && args(userCredentials)")
    void logRegistration(UserRegistrationRequest userCredentials) {
        log.info("User {} is making new account", userCredentials.getUsername());
    }

}
