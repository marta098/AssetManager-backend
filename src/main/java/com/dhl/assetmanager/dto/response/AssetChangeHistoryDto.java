package com.dhl.assetmanager.dto.response;

import com.dhl.assetmanager.entity.AssetStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AssetChangeHistoryDto implements Serializable {
    private final UserDto owner;
    private final UserDto author;
    private final LocalDateTime timestamp;
    private final LocalDateTime assignmentDate;
    private final LocalDateTime dischargeDate;
    private final LocalDateTime deprecation;
    private final String remark;
    private final AssetStatus status;
}
