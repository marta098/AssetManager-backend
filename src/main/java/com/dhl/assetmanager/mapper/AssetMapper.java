package com.dhl.assetmanager.mapper;

import com.dhl.assetmanager.dto.response.*;
import com.dhl.assetmanager.entity.Asset;
import com.dhl.assetmanager.entity.Location;
import com.dhl.assetmanager.entity.Order;
import com.dhl.assetmanager.entity.Stockroom;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        uses = {UserMapper.class, AssetChangeHistoryMapper.class})
public abstract class AssetMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AssetChangeHistoryMapper assetChangeHistoryMapper;

    public AssetDto assetToAssetDto(Asset asset) {
        if (asset == null) {
            return null;
        }

        var lastOrder = getLastOrder(asset)
                .orElse(null);

        return AssetDto.builder()
                .id(asset.getId())
                .serialNumber(asset.getSerialNumber())
                .model(asset.getModel())
                .type(asset.getType())
                .deprecation(asset.getDeprecation())
                .currentUser(userMapper.userToUserDto(asset.getCurrentUser()))
                .status(asset.getStatus())
                .location(locationToLocationDto(asset.getLocation()))
                .stockroom(stockroomToStockroomDto(asset.getStockroom()))
                .crestCode(asset.getCrestCode())
                .name(asset.getName())
                .mpkNumber(lastOrder != null ? lastOrder.getMpkNumber().getMpk() : null)
                .createdBy(lastOrder != null ? userMapper.userToUserDto(lastOrder.getRequester()) : null)

                .build();
    }

    public abstract List<AssetDto> assetListToAssetDto(List<Asset> asset);

    public abstract SerialNumberCheckResponse assetToSerialNumberCheckResponse(Asset asset);

    public AssetDetailedDto assetToAssetDetailedDto(Asset asset) {
        var lastOrder = getLastOrder(asset)
                .orElse(null);
        var changeHistory = asset.getChangeHistory().stream()
                .map(assetChangeHistoryMapper::assetChangeHistoryToAssetChangeHistoryDto)
                .collect(Collectors.toList());
        var orders = asset.getOrders().stream()
                .map(this::orderToAssetOrderChangeHistoryDto)
                .collect(Collectors.toList());

        return AssetDetailedDto.builder()
                .id(asset.getId())
                .serialNumber(asset.getSerialNumber())
                .model(asset.getModel())
                .type(asset.getType())
                .deprecation(asset.getDeprecation())
                .currentUser(userMapper.userToUserDto(asset.getCurrentUser()))
                .status(asset.getStatus())
                .location(locationToLocationDto(asset.getLocation()))
                .stockroom(stockroomToStockroomDto(asset.getStockroom()))
                .crestCode(asset.getCrestCode())
                .name(asset.getName())
                .mpkNumber(lastOrder != null ? lastOrder.getMpkNumber().getMpk() : null)
                .createdBy(lastOrder != null ? userMapper.userToUserDto(lastOrder.getRequester()) : null)
                .orders(orders)
                .changeHistory(changeHistory)
                .build();
    }

    abstract AssetOrderChangeHistoryDto orderToAssetOrderChangeHistoryDto(Order order);

    private Optional<Order> getLastOrder(Asset asset) {
        return asset.getOrders().stream()
                .max(Comparator.comparing(Order::getAddedDate));
    }

    abstract LocationDto locationToLocationDto(Location location);

    abstract StockroomDto stockroomToStockroomDto(Stockroom stockroom);

}
