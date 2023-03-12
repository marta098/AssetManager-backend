package com.dhl.assetmanager.dto.response;

import com.dhl.assetmanager.entity.AssetStatus;
import com.dhl.assetmanager.entity.AssetType;
import com.dhl.assetmanager.entity.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class AssetDto implements Serializable {
    private final long id;
    private final String serialNumber;
    private final Model model;
    private final AssetType type;
    private final LocalDateTime deprecation;
    private final UserDto currentUser;
    private final AssetStatus status;
    private final LocationDto location;
    private final StockroomDto stockroom;
    private final String crestCode;
    private final String name;
    private final String mpkNumber;
    private final UserDto createdBy;
}
