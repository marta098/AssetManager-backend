package com.dhl.assetmanager.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RemindersSchedulerLogger {

    @Around("execution(* com.dhl.assetmanager.scheduler.RemindersScheduler.scanForReminders())")
    Object logSendingStatusUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Scanning for reminders");
        var result = joinPoint.proceed();
        log.info("Scan completed");
        return result;
    }

}
