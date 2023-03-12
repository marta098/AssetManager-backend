package com.dhl.assetmanager.dto.request;

import com.dhl.assetmanager.entity.DeliveryType;
import com.dhl.assetmanager.entity.Model;
import com.dhl.assetmanager.entity.Status;
import com.dhl.assetmanager.validation.EnumValues;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderUpdateRequest implements Serializable {
    @NotNull
    private final LocalDateTime pickupDate;
    private String serialNumber;
    private String remark;
    @EnumValues(enumClass = Status.class)
    private String status;
    @NotNull
    private long receiverId;
    @NotNull
    private Model requestedModel;
    private String deliveryAddress;
    private DeliveryType deliveryType;
    private long assignedTo;
    private long assignedBy;
}
