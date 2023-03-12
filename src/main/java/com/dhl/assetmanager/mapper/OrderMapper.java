package com.dhl.assetmanager.mapper;

import com.dhl.assetmanager.dto.request.NewOrderRequest;
import com.dhl.assetmanager.dto.response.ChangeHistoryDto;
import com.dhl.assetmanager.dto.response.OrderDto;
import com.dhl.assetmanager.entity.*;
import com.dhl.assetmanager.util.DateUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring",
        uses = {AssetMapper.class, UserMapper.class})
public abstract class OrderMapper {

    @Autowired
    private DateUtil dateUtil;

    @Mapping(source = "mpkNumber.mpk", target = "mpkNumber")
    public abstract OrderDto orderToOrderDto(Order order);

    public abstract ChangeHistoryDto changeHistoryToChangeHistoryDto(ChangeHistory changeHistory);

    public Order orderDtoToOrder(NewOrderRequest orderDto, User receiver, User requester, String orderNumber, MpkNumber mpkNumber) {
        return Order.builder()
                .orderNumber(orderNumber)
                .status(Status.NEW)
                .addedDate(dateUtil.getCurrentDateTime())
                .receiver(receiver)
                .requester(requester)
                .pickupDate(orderDto.getPickupDate())
                .remark(orderDto.getRemark())
                .requestedModel(orderDto.getModel())
                .mpkNumber(mpkNumber)
                .deliveryType(orderDto.getDeliveryType())
                .deliveryAddress(orderDto.getDeliveryAddress())
                .changeHistory(Collections.emptyList())
                .isDhlReminderSent(false)
                .isItReminderSent(false)
                .build();
    }

}
