package com.dhl.assetmanager.dto.request;

import com.dhl.assetmanager.entity.AssetStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AssetUpdateRequest implements Serializable {
    private final AssetStatus status;
    private final String remark;
    private final LocalDateTime deprecation;
    private final Long currentUserId;
    private final Long locationId;
    private final Long stockroomId;
}
