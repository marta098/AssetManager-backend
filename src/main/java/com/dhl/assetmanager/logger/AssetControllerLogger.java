package com.dhl.assetmanager.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AssetControllerLogger {

    @Before("execution(* com.dhl.assetmanager.controller.AssetController.getAsset(..)) && args(assetId, ..)")
    void logGettingAsset(long assetId) {
        log.info("Getting asset with id {}", assetId);
    }

    @Before("execution(* com.dhl.assetmanager.controller.AssetController.getAllUsersAssets(..)) && args(userId, ..)")
    void logGettingAllUsersAssets(long userId) {
        log.info("Getting all assets for user with id {}", userId);
    }

}
