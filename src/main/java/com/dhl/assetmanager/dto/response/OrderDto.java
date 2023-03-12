package com.dhl.assetmanager.dto.response;

import com.dhl.assetmanager.entity.DeliveryType;
import com.dhl.assetmanager.entity.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class OrderDto implements Serializable {
    private final long id;
    private final String orderNumber;
    private final String mpkNumber;
    private final Model requestedModel;
    private final String status;
    private final LocalDateTime addedDate;
    private final LocalDateTime assignmentDate;
    private final UserDto assignedTo;
    private final UserDto requester;
    private final UserDto assignedBy;
    private final UserDto receiver;
    private final LocalDateTime pickupDate;
    private final String deliveryAddress;
    private final String remark;
    private final AssetDto asset;
    private final DeliveryType deliveryType;
    private final List<ChangeHistoryDto> changeHistory;
    private final String receiver_StringType;
}
