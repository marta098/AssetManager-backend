package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.request.AssetUpdateRequest;
import com.dhl.assetmanager.dto.response.*;
import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
class AssetController {

    private final AssetService assetService;

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @GetMapping
    public ResponseEntity<List<AssetDto>> getAllAssets() {
        var allAssets = assetService.getAllAssets();
        return ResponseEntity.ok(allAssets);
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<AssetDetailedDto> getAsset(@PathVariable long assetId) {
        var asset = assetService.getAsset(assetId);
        return ResponseEntity.ok(asset);
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @PutMapping("/{assetId}")
    public ResponseEntity<AssetDetailedDto> updateAsset(@PathVariable long assetId, @RequestBody AssetUpdateRequest assetUpdateRequest,
                                                             Authentication authentication) {
        var assetChangeHistory = assetService.updateAsset(assetId, assetUpdateRequest, (User) authentication.getPrincipal());
        return ResponseEntity.ok(assetChangeHistory);
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @GetMapping("/serial-numbers/{serialNumber}")
    public ResponseEntity<SerialNumberCheckResponse> checkSerialNumberStatus(@PathVariable String serialNumber) {
        var serialNumberCheckResponse = assetService.checkSerialNumber(serialNumber);
        return ResponseEntity.ok(serialNumberCheckResponse);
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @GetMapping("/serial-numbers/{serialNumber}/orders-history")
    public ResponseEntity<List<OrderDto>> getHistoryOfOrders(@PathVariable String serialNumber) {
        var historyOfOrders = assetService.getHistoryOfOrders(serialNumber);
        return ResponseEntity.ok(historyOfOrders);
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @GetMapping("/asset-id/{assetId}/orders-history")
    public ResponseEntity<List<OrderDto>> getHistoryOfOrdersById(@PathVariable long assetId) {
        var historyOfOrders = assetService.getHistoryOfOrdersById(assetId);
        return ResponseEntity.ok(historyOfOrders);
    }

    @PreAuthorize("hasAnyRole('MANAGER_DHL', 'EMPLOYEE_DHL')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<AssetDto>> getAllUsersAssets(@PathVariable long userId, Authentication authentication) {
        var assets = assetService.getAllUsersAssets(userId, (User) authentication.getPrincipal());
        return ResponseEntity.ok(assets);
    }

}
