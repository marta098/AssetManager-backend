package com.dhl.assetmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class AssetOrderChangeHistoryDto implements Serializable {
    private final long id;
    private final String orderNumber;
    private final UserDto requester;
}
