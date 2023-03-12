package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.request.AssetUpdateRequest;
import com.dhl.assetmanager.dto.response.*;
import com.dhl.assetmanager.entity.Asset;
import com.dhl.assetmanager.entity.AssetChangeHistory;
import com.dhl.assetmanager.entity.AssetStatus;
import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.exception.*;
import com.dhl.assetmanager.mapper.AssetChangeHistoryMapper;
import com.dhl.assetmanager.mapper.AssetMapper;
import com.dhl.assetmanager.mapper.OrderMapper;
import com.dhl.assetmanager.repository.AssetRepository;
import com.dhl.assetmanager.repository.LocationRepository;
import com.dhl.assetmanager.repository.StockroomRepository;
import com.dhl.assetmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final StockroomRepository stockroomRepository;

    private final AssetMapper assetMapper;
    private final OrderMapper orderMapper;
    private final AssetChangeHistoryMapper assetChangeHistoryMapper;

    public List<AssetDto> getAllAssets() {
        var assets = assetRepository.findAll();
        return assetMapper.assetListToAssetDto(assets);
    }

    public AssetDetailedDto getAsset(long assetId) {
        var asset = assetRepository.findById(assetId).orElseThrow(AssetNotFoundException::new);
        return assetMapper.assetToAssetDetailedDto(asset);
    }

    @Transactional
    public AssetDetailedDto updateAsset(long assetId, AssetUpdateRequest assetUpdateRequest, User createdByUser) {
        validateUpdateRequest(assetUpdateRequest);
        var asset = assetRepository.findById(assetId).orElseThrow(AssetNotFoundException::new);
        User newUser = null;
        if (assetUpdateRequest.getCurrentUserId() != null) {
            newUser = userRepository.findById(assetUpdateRequest.getCurrentUserId())
                    .orElseThrow(UserNotFoundException::new);
        }

        var assetChangeHistory = createAssetChangeHistory(asset, assetUpdateRequest, newUser, createdByUser);
        asset.getChangeHistory().add(assetChangeHistory);
        if (assetUpdateRequest.getStatus() == AssetStatus.IN_USE) {
            var location = locationRepository.findById(assetUpdateRequest.getLocationId())
                    .orElseThrow(LocationNotFoundException::new);
            asset.setLocation(location);
            asset.setStockroom(null);
        } else {
            var stockroom = stockroomRepository.findById(assetUpdateRequest.getStockroomId())
                    .orElseThrow(StockroomNotFoundException::new);
            asset.setStockroom(stockroom);
            asset.setLocation(null);
        }
        asset.setStatus(assetUpdateRequest.getStatus());
        asset.setCurrentUser(newUser);
        asset.setDeprecation(assetUpdateRequest.getDeprecation());
        assetRepository.save(asset);
//        return assetChangeHistoryMapper.assetChangeHistoryToAssetChangeHistoryDto(assetChangeHistory);
        return assetMapper.assetToAssetDetailedDto(asset);
    }

    public List<AssetDto> getAllUsersAssets(long userId, User user) {
        if (userId != user.getId()) {
            throw new UserNotPermittedException();
        }

        var assets = assetRepository.findAllByCurrentUser_Id(userId);
        return assetMapper.assetListToAssetDto(assets);
    }

    public SerialNumberCheckResponse checkSerialNumber(String serialNumber) {
        var asset = assetRepository.findBySerialNumber(serialNumber)
                .orElseThrow(AssetNotFoundException::new);
        return assetMapper.assetToSerialNumberCheckResponse(asset);
    }

    public List<OrderDto> getHistoryOfOrders(String serialNumber) {
        var asset = assetRepository.findBySerialNumber(serialNumber)
                .orElseThrow(AssetNotFoundException::new);

        return asset.getOrders().stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getHistoryOfOrdersById(long assetId) {
        var asset = assetRepository.findById(assetId)
                .orElseThrow(AssetNotFoundException::new);

        return asset.getOrders().stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList());
    }

    private void validateUpdateRequest(AssetUpdateRequest assetUpdateRequest) {
        if (assetUpdateRequest.getStatus() == AssetStatus.IN_USE) {
            if (assetUpdateRequest.getCurrentUserId() == null || assetUpdateRequest.getLocationId() == null) {
                throw new AssetIllegalArgumentException();
            }
        } else {
            if (assetUpdateRequest.getStockroomId() == null) {
                throw new AssetIllegalArgumentException();
            }
        }
    }

    private AssetChangeHistory createAssetChangeHistory(Asset asset, AssetUpdateRequest assetUpdateRequest, User newUser, User createdByUser) {
        var currentTimestamp = LocalDateTime.now();
        var previousUser = asset.getCurrentUser();

        if (previousUser == null) {
            return AssetChangeHistory.builder()
                    .asset(asset)
                    .owner(newUser)
                    .author(createdByUser)
                    .timestamp(currentTimestamp)
                    .assignmentDate(newUser != null ? currentTimestamp : null)
                    .remark(assetUpdateRequest.getRemark())
                    .deprecation(assetUpdateRequest.getDeprecation())
                    .status(assetUpdateRequest.getStatus())
                    .build();
        }
        var changeHistory = asset.getChangeHistory().stream()
                .max(Comparator.comparing(AssetChangeHistory::getTimestamp))
                .orElse(null);

        if (newUser == null) {
            if (changeHistory != null) {
                changeHistory.setDischargeDate(currentTimestamp);
            }
            return AssetChangeHistory.builder()
                    .asset(asset)
                    .author(createdByUser)
                    .timestamp(currentTimestamp)
                    .remark(assetUpdateRequest.getRemark())
                    .deprecation(assetUpdateRequest.getDeprecation())
                    .status(assetUpdateRequest.getStatus())
                    .build();
        }

        if (previousUser.getId() == newUser.getId()) {
            return AssetChangeHistory.builder()
                    .asset(asset)
                    .owner(previousUser)
                    .author(createdByUser)
                    .timestamp(currentTimestamp)
                    .assignmentDate(changeHistory != null ? changeHistory.getAssignmentDate() : currentTimestamp)
                    .remark(assetUpdateRequest.getRemark())
                    .deprecation(assetUpdateRequest.getDeprecation())
                    .status(assetUpdateRequest.getStatus())
                    .build();
        }

        if (changeHistory != null) {
            changeHistory.setDischargeDate(currentTimestamp);
        }

        return AssetChangeHistory.builder()
                .asset(asset)
                .owner(newUser)
                .author(createdByUser)
                .timestamp(currentTimestamp)
                .assignmentDate(currentTimestamp)
                .remark(assetUpdateRequest.getRemark())
                .deprecation(assetUpdateRequest.getDeprecation())
                .status(assetUpdateRequest.getStatus())
                .build();
    }

}