package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.response.AssetStatisticsResponse;
import com.dhl.assetmanager.dto.response.EmployeeWorkloadDto;
import com.dhl.assetmanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PreAuthorize("hasRole('MANAGER_IT')")
    @GetMapping("/workload")
    public ResponseEntity<List<EmployeeWorkloadDto>> getEmployeesWorkloadData() {
        var employeesWorkloadData = dashboardService.getEmployeesWorkloadData();
        return ResponseEntity.ok(employeesWorkloadData);
    }

    @PreAuthorize("hasRole('MANAGER_IT')")
    @GetMapping("/asset-statistics")
    public ResponseEntity<AssetStatisticsResponse> getAssetStatistics() {
        var assetStatistics = dashboardService.getAssetStatistics();
        return ResponseEntity.ok(assetStatistics);
    }

}
