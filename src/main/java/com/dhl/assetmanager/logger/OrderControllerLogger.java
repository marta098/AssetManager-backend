package com.dhl.assetmanager.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class OrderControllerLogger {

    @Before("execution(* com.dhl.assetmanager.controller.OrderController.getAllOrders(..))")
    void logGettingAllOrders() {
        log.info("Getting all orders");
    }

    @Before("execution(* com.dhl.assetmanager.controller.OrderController.createOrder(..))")
    void logCreatingNewOrder() {
        log.info("Creating new order");
    }

    @Before("execution(* com.dhl.assetmanager.controller.OrderController.getOrderDetails(..)) && args(orderId, ..)")
    void logGettingOrder(long orderId) {
        log.info("Getting order with id {}", orderId);
    }

    @Before("execution(* com.dhl.assetmanager.controller.OrderController.updateOrder(..)) && args(orderId, ..)")
    void logUpdatingOrder(long orderId) {
        log.info("Updating order with id {}", orderId);
    }

    @Before("execution(* com.dhl.assetmanager.controller.OrderController.changeStatus(..)) && args(orderId, ..)")
    void logChangingStatus(long orderId) {
        log.info("Changing status to order with id {}", orderId);
    }

    @Before("execution(* com.dhl.assetmanager.controller.OrderController.assignUser(..)) && args(orderId, ..)")
    void logAssigningUser(long orderId) {
        log.info("Assigning user to order with id {}", orderId);
    }

    @Before("execution(* com.dhl.assetmanager.controller.OrderController.addRemark(..)) && args(orderId, ..)")
    void logChangingRemark(long orderId) {
        log.info("Changing remark to order with id {}", orderId);
    }

}
