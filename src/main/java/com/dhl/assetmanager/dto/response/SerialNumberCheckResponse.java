package com.dhl.assetmanager.dto.response;

import com.dhl.assetmanager.entity.AssetStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SerialNumberCheckResponse {
    private final String serialNumber;
    private final String model;
    private final UserDto currentUser;
    private final AssetStatus status;
}
