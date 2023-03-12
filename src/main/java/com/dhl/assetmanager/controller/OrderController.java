package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.request.*;
import com.dhl.assetmanager.dto.response.OrderDto;
import com.dhl.assetmanager.entity.Status;
import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('EMPLOYEE_IT')")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        var allOrders = orderService.getAllOrders();
        return ResponseEntity.ok(allOrders);
    }

    @PreAuthorize("hasRole('MANAGER_DHL')")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Validated NewOrderRequest newOrderDto, Authentication authentication) {
        var orderDto = orderService.addNewOrder(newOrderDto, (User) authentication.getPrincipal());
        return ResponseEntity.ok(orderDto);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE_IT', 'MANAGER_DHL', 'EMPLOYEE_DHL')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderDetails(@PathVariable long orderId, Authentication authentication) {
        var orderDto = orderService.getOrderDetails(orderId, (User) authentication.getPrincipal());
        return ResponseEntity.ok(orderDto);
    }

    @PreAuthorize("hasRole('EMPLOYEE_IT')")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable long orderId,
                                                @RequestBody @Validated OrderUpdateRequest orderUpdateRequest,
                                                Authentication authentication) {
        var orderDto = orderService.updateOrder(orderId, orderUpdateRequest, (User) authentication.getPrincipal());
        return ResponseEntity.ok(orderDto);
    }

    @PreAuthorize("hasRole('EMPLOYEE_IT')")
    @PutMapping("/{orderId}/new-asset")
    public ResponseEntity<OrderDto> updateOrderWithNewAsset(@PathVariable long orderId,
                                                            @RequestBody @Validated OrderUpdateNewAssetRequest orderUpdateRequest,
                                                            Authentication authentication) {
        var orderDto = orderService.updateOrderWithNewAsset(orderId, orderUpdateRequest, (User) authentication.getPrincipal());
        return ResponseEntity.ok(orderDto);
    }

    @PreAuthorize("hasRole('EMPLOYEE_IT')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable long orderId,
                                             @RequestBody @Validated StatusChangeRequest statusChangeRequest,
                                             Authentication authentication) {
        orderService.changeStatus(orderId, Status.valueOf(statusChangeRequest.getNewStatus()), (User) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('EMPLOYEE_IT')")
    @PutMapping("/{orderId}/assign/{userId}")
    public ResponseEntity<LocalDateTime> assignUser(@PathVariable long orderId, @PathVariable long userId, Authentication authentication) {
        var latestChange = orderService.assignUserToOrder(orderId, userId, (User) authentication.getPrincipal());
        return ResponseEntity.ok(latestChange);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE_IT', 'MANAGER_DHL', 'EMPLOYEE_DHL')")
    @PutMapping("/{orderId}/remark")
    public ResponseEntity<Void> addRemark(@PathVariable long orderId,
                                          @RequestBody @Validated RemarkChangeRequest remarkChangeRequest,
                                          Authentication authentication) {
        orderService.addRemark(orderId, remarkChangeRequest.getRemark(), (User) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

}
