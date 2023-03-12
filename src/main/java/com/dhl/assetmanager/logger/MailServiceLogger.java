package com.dhl.assetmanager.logger;

import com.dhl.assetmanager.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MailServiceLogger {

    @Before("execution(* com.dhl.assetmanager.service.MailService.sendChangingStatusUpdate(..)) && args(order)")
    void logSendingStatusUpdate(Order order) {
        log.info("Sending status update mail for order with id {}", order.getId());
    }

    @Before("execution(* com.dhl.assetmanager.service.MailService.sendNewOrder(..)) && args(order)")
    void logSendingNewOrder(Order order) {
        log.info("Sending new order mail for order with id {}", order.getId());
    }

    @Before("execution(* com.dhl.assetmanager.service.MailService.sendDhlReminder(..)) && args(order)")
    void logSendingDhlReminder(Order order) {
        log.info("Sending dhl reminder for order with id {}", order.getId());
    }

    @Before("execution(* com.dhl.assetmanager.service.MailService.sendItReminder(..)) && args(order)")
    void logSendingItReminder(Order order) {
        log.info("Sending it reminder for order with id {}", order.getId());
    }

}
