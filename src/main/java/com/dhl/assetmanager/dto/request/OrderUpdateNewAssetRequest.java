package com.dhl.assetmanager.dto.request;

import com.dhl.assetmanager.entity.DeliveryType;
import com.dhl.assetmanager.entity.Model;
import com.dhl.assetmanager.entity.Status;
import com.dhl.assetmanager.validation.EnumValues;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderUpdateNewAssetRequest {
    private final LocalDateTime pickupDate;
    private String remark;
    @NotNull
    private NewAssetRequest asset;
    @EnumValues(enumClass = Status.class)
    private String status;
    private long receiverId;
    private Model requestedModel;
    private String deliveryAddress;
    private DeliveryType deliveryType;
    private long assignedTo;
    private long assignedBy;
}
