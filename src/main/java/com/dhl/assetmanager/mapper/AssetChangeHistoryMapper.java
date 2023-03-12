package com.dhl.assetmanager.mapper;

import com.dhl.assetmanager.dto.response.AssetChangeHistoryDto;
import com.dhl.assetmanager.entity.AssetChangeHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = UserMapper.class)
public interface AssetChangeHistoryMapper {

    AssetChangeHistoryDto assetChangeHistoryToAssetChangeHistoryDto(AssetChangeHistory assetChangeHistory);

}
