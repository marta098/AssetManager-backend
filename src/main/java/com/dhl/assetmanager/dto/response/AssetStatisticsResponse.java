package com.dhl.assetmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AssetStatisticsResponse {
    private final int inUseCount;
    private final int inStockCount;
    private final int deprecatedCount;
    private final int allAssetCount;
}
