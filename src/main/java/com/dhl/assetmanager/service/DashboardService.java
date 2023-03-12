package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.response.AssetStatisticsResponse;
import com.dhl.assetmanager.dto.response.EmployeeWorkloadDto;
import com.dhl.assetmanager.entity.AssetStatus;
import com.dhl.assetmanager.entity.Order;
import com.dhl.assetmanager.entity.Status;
import com.dhl.assetmanager.exception.RoleNotFoundException;
import com.dhl.assetmanager.repository.AssetRepository;
import com.dhl.assetmanager.repository.RoleRepository;
import com.dhl.assetmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final String IT_EMPLOYEE = "ROLE_EMPLOYEE_IT";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AssetRepository assetRepository;

    public List<EmployeeWorkloadDto> getEmployeesWorkloadData() {
        var role = roleRepository.findByName(IT_EMPLOYEE).orElseThrow(() -> new RoleNotFoundException(false));
        var employees = userRepository.findAllByRole(role);

        return employees.stream()
                .map(employee -> EmployeeWorkloadDto.builder()
                        .username(employee.getUsername())
                        .assignedOrders(getActiveOrdersSize(employee.getAssignedToOrders()))
                        .build())
                .collect(Collectors.toList());
    }

    public AssetStatisticsResponse getAssetStatistics() {
        var inUseCount = assetRepository.countAllByStatus(AssetStatus.IN_USE);
        var inStockCount = assetRepository.countAllByStatus(AssetStatus.IN_STOCK);
        var deprecatedCount = assetRepository.countDeprecatedAssets();
        var allAssetsCount = assetRepository.count();

        return AssetStatisticsResponse.builder()
                .inUseCount(inUseCount)
                .inStockCount(inStockCount)
                .deprecatedCount(deprecatedCount)
                .allAssetCount(allAssetsCount)
                .build();
    }

    private long getActiveOrdersSize(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() != Status.COMPLETED)
                .count();
    }

}
