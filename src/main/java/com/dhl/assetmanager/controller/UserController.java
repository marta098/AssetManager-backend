package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.request.UserRoleRequest;
import com.dhl.assetmanager.dto.response.OrderDto;
import com.dhl.assetmanager.dto.response.UserDto;
import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT', 'MANAGER_DHL')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        var allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @PreAuthorize("hasRole('MANAGER_IT')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT', 'MANAGER_DHL')")
    @GetMapping("/{role}")
    public ResponseEntity<List<UserDto>> getAllUsersWithRole(@PathVariable String role) {
        var allUsers = userService.getAllUsersWithRole(role);
        return ResponseEntity.ok(allUsers);
    }

    @PreAuthorize("hasAnyRole('MANAGER_IT', 'EMPLOYEE_IT')")
    @PutMapping("/{userId}/roles")
    public ResponseEntity<Void> changeRole(@PathVariable long userId, @RequestBody @Validated UserRoleRequest userRoleRequest) {
        userService.changeRole(userId, userRoleRequest.getRole());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderDto>> getAllUsersOrdersByReceiver(@PathVariable long userId) {
        var allOrders = userService.getAllOrdersByReceiver(userId);
        return ResponseEntity.ok(allOrders);
    }

    @PreAuthorize("hasRole('MANAGER_DHL')")
    @GetMapping("/{userId}/created-orders")
    public ResponseEntity<List<OrderDto>> getOrdersCreatedByUser(@PathVariable long userId, Authentication authentication) {
        var allOrders = userService.getOrdersCreatedByUser(userId, (User) authentication.getPrincipal());
        return ResponseEntity.ok(allOrders);
    }

}
