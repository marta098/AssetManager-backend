package com.dhl.assetmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
public class EmployeeWorkloadDto implements Serializable {
    private final String username;
    private final long assignedOrders;
}
