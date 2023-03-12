package com.dhl.assetmanager.dto.request;

import com.dhl.assetmanager.entity.DeliveryType;
import com.dhl.assetmanager.entity.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewOrderRequest implements Serializable {

    @NotNull
    private final String receiverEmail;
    @NotNull
    private final LocalDateTime pickupDate;
    private final String remark;
    @NotNull
    private final long mpkId;
    @NotNull
    private final Model model;
    @NotNull
    private final DeliveryType deliveryType;
    private final String deliveryAddress;

}
