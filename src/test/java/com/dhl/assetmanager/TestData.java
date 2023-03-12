package com.dhl.assetmanager;

import com.dhl.assetmanager.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class TestData {

    public static Order exampleOrder(long index, String orderNumber) {
        return new Order(index,
                orderNumber,
                new MpkNumber(1, "test"),
                Model.LARGE_LAPTOP,
                Status.NEW,
                LocalDateTime.now(),
                null,
                itEmployee(),
                itEmployee(),
                itManager(),
                dhlEmployee(3),
                LocalDateTime.now(),
                "Test address",
                null,
                exampleAsset(99L),
                DeliveryType.PICKUP,
                new ArrayList<>(),
                false,
                false);
    }

    public static Order exampleOrder(long index) {
        return exampleOrder(index, "2022-01-01-99");
    }

    public static Asset exampleAsset(long id) {
        return exampleAssetWithCurrentUser(id, null);
    }

    public static Asset exampleAssetWithCurrentUser(long id, User user) {
        return new Asset(id,
                "test",
                Model.LARGE_LAPTOP,
                AssetType.LAPTOP,
                LocalDateTime.MAX,
                user,
                AssetStatus.IN_USE,
                new Location(1L, "test"),
                new Stockroom(1L, "test"),
                "test",
                "test",
                Collections.emptyList(),
                Collections.emptyList());
    }

    public static User itManager() {
        return new User(1,
                "ItManager",
                "ItManager@test.test",
                "$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe",
                new Role(0L, "ROLE_MANAGER_IT"),
                Collections.emptyList(),
                null,
                null,
                null,
                Collections.emptyList(),
                false);
    }

    public static User itEmployee() {
        return new User(2,
                "ItEmployee",
                "ItEmployee@test.test",
                "$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe",
                new Role(1L, "ROLE_EMPLOYEE_IT"),
                Collections.emptyList(),
                null,
                null,
                null,
                Collections.emptyList(),
                false);
    }

    public static User dhlManager() {
        return new User(4,
                "DhlManager",
                "DhlManager@test.test",
                "$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe",
                new Role(1L, "ROLE_MANAGER_DHL"),
                Collections.emptyList(),
                null,
                null,
                null,
                Collections.emptyList(),
                false);
    }

    public static User dhlEmployee(long userId) {
        return new User(userId,
                "DhlEmployee",
                "DhlEmployee@test.test",
                "$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe",
                new Role(1L, "ROLE_EMPLOYEE_DHL"),
                Collections.emptyList(),
                null,
                null,
                null,
                Collections.emptyList(),
                false);
    }
}
