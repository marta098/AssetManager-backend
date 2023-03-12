package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.response.OrderDto;
import com.dhl.assetmanager.dto.response.UserDto;
import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.exception.RoleNotFoundException;
import com.dhl.assetmanager.exception.UserNotFoundException;
import com.dhl.assetmanager.exception.UserNotPermittedException;
import com.dhl.assetmanager.mapper.OrderMapper;
import com.dhl.assetmanager.mapper.UserMapper;
import com.dhl.assetmanager.repository.OrderRepository;
import com.dhl.assetmanager.repository.RoleRepository;
import com.dhl.assetmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;

    public List<UserDto> getAllUsers() {
        var allUsers = userRepository.findByOrderByRoleAsc();
        return userMapper.userListToUserDtoList(allUsers);
    }

    @Transactional
    public void deleteUser(long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    public List<UserDto> getAllUsersWithRole(String roleName) {
        var role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException(true));
        var allUsers = userRepository.findAllByRole(role);
        return userMapper.userListToUserDtoList(allUsers);
    }

    @Transactional
    public void changeRole(long userId, String roleName) {
        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(true));

        user.setRole(role);
        userRepository.save(user);
    }

    public List<OrderDto> getAllOrdersByReceiver(long userId) {
        var orders = orderRepository.findAllByReceiver_IdOrderByAddedDateDesc(userId);
        return orders.stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getOrdersCreatedByUser(long userId, User user) {
        if (userId != user.getId()) {
            throw new UserNotPermittedException();
        }

        return orderRepository.findAllByRequester_IdOrderByAddedDateDesc(userId).stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList());
    }
}
