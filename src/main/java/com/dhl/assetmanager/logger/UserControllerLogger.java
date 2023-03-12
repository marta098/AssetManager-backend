package com.dhl.assetmanager.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UserControllerLogger {

    @Before("execution(* com.dhl.assetmanager.controller.UserController.getAllUsers(..))")
    void logGettingAllUsers() {
        log.info("Getting all users");
    }

    @Before("execution(* com.dhl.assetmanager.controller.UserController.deleteUser(..)) && args(userId)")
    void logDeletingUser(long userId) {
        log.info("Deleting users with id {}", userId);
    }

    @Before("execution(* com.dhl.assetmanager.controller.UserController.getAllUsersWithRole(..)) && args(role)")
    void logGettingAllUsersWithRole(String role) {
        log.info("Getting all users with role {}", role);
    }

    @Before("execution(* com.dhl.assetmanager.controller.UserController.changeRole(..)) && args(userId, ..)")
    void logChangingRole(long userId) {
        log.info("Changing role to user with id {}", userId);
    }

    @Before("execution(* com.dhl.assetmanager.controller.UserController.getAllUsersOrdersByReceiver(..)) && args(userId)")
    void logGettingAllUsersOrders(long userId) {
        log.info("Getting all orders by receiver with id {}", userId);
    }

}
