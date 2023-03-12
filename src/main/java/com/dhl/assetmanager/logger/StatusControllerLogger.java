package com.dhl.assetmanager.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class StatusControllerLogger {

    @Before("execution(* com.dhl.assetmanager.controller.StatusController.getAllStatuses(..))")
    void logGettingAllStatuses() {
        log.info("Getting all statuses");
    }

}
