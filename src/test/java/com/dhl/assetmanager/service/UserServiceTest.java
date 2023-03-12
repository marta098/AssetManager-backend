package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.response.OrderDto;
import com.dhl.assetmanager.dto.response.UserDto;
import com.dhl.assetmanager.entity.Role;
import com.dhl.assetmanager.exception.RoleNotFoundException;
import com.dhl.assetmanager.exception.UserNotFoundException;
import com.dhl.assetmanager.repository.OrderRepository;
import com.dhl.assetmanager.repository.RoleRepository;
import com.dhl.assetmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static com.dhl.assetmanager.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private OrderRepository orderRepository;

    @Test
    void shouldReturnAllUsers() {
        when(userRepository.findByOrderByRoleAsc()).thenReturn(List.of(itManager(), itEmployee(), dhlEmployee(3)));

        var actualUsers = userService.getAllUsers();

        assertThat(actualUsers)
                .hasSize(3)
                .hasOnlyElementsOfTypes(UserDto.class);
    }

    @Test
    void shouldChangeRole() {
        when(userRepository.findById(999L)).thenReturn(Optional.of(dhlEmployee(3)));
        when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(new Role(99L, "TEST_ROLE")));

        userService.changeRole(999L, "TEST_ROLE");

        verify(userRepository).save(argThat(user -> user.getRole().getName().equals("TEST_ROLE")));
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIsNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.changeRole(999L, "TEST_ROLE"));
    }

    @Test
    void shouldThrowRoleNotFoundExceptionWhenRoleIsNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.of(dhlEmployee(3)));
        when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.changeRole(999L, "TEST_ROLE"));
    }

    @Test
    void shouldReturnAllUsersForGivenRole() {
        var givenRoleName = "role_employee_it";
        var givenRole = new Role(99L, "ROLE_EMPLOYEE_IT");

        when(roleRepository.findByName("ROLE_EMPLOYEE_IT")).thenReturn(Optional.of(givenRole));
        when(userRepository.findAllByRole(givenRole)).thenReturn(List.of(itEmployee(), itEmployee()));

        var actualUsers = userService.getAllUsersWithRole(givenRoleName);

        assertThat(actualUsers)
                .hasSize(2)
                .hasOnlyElementsOfTypes(UserDto.class);
    }

    @Test
    void shouldThrowRoleNotFoundExceptionWhenGettingAllUsersWithWrongRoleName() {
        var givenRoleName = "wrong_role";

        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.getAllUsersWithRole(givenRoleName));
    }

    @Test
    void shouldReturnOrdersForGivenUserId() {
        var givenUserId = 99L;

        when(orderRepository.findAllByReceiver_IdOrderByAddedDateDesc(99L)).thenReturn(List.of(exampleOrder(1L), exampleOrder(2L)));

        var actualOrders = userService.getAllOrdersByReceiver(givenUserId);

        assertThat(actualOrders)
                .hasSize(2)
                .hasOnlyElementsOfType(OrderDto.class);
    }

}
