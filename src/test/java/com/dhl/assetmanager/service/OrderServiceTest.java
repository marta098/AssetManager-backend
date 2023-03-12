package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.request.NewOrderRequest;
import com.dhl.assetmanager.entity.*;
import com.dhl.assetmanager.exception.OrderNotFoundException;
import com.dhl.assetmanager.exception.UserIsNotEmployeeItException;
import com.dhl.assetmanager.exception.UserNotFoundException;
import com.dhl.assetmanager.exception.UserNotPermittedException;
import com.dhl.assetmanager.repository.AssetRepository;
import com.dhl.assetmanager.repository.MpkNumberRepository;
import com.dhl.assetmanager.repository.OrderRepository;
import com.dhl.assetmanager.repository.UserRepository;
import com.dhl.assetmanager.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.dhl.assetmanager.TestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AssetRepository assetRepository;
    @MockBean
    private MpkNumberRepository mpkNumberRepository;
    @MockBean
    private DateUtil dateUtil;
    @MockBean
    private MailService mailService;

    @Test
    void shouldAddNewOrderWithIncreasedOrderNumber() {
        var givenOrder = prepareOrderRequest();
        var givenUser = itEmployee();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(dhlEmployee(3)));
        when(mpkNumberRepository.findById(anyLong())).thenReturn(Optional.of(new MpkNumber(1L, "test")));
        when(orderRepository.findAllOrdersCreatedToday()).thenReturn(List.of(exampleOrder(99L, "2022-01-01-99")));
        when(dateUtil.getCurrentDate()).thenReturn(LocalDate.of(2022, 1, 1));

        orderService.addNewOrder(givenOrder, givenUser);

        verify(orderRepository).save(argThat(order -> order.getOrderNumber().equals("2022-01-01-100")));
        verify(mailService).sendNewOrder(any());
    }

    @Test
    void shouldAddNewOrderWithFirstOrderNumber() {
        var givenOrder = prepareOrderRequest();
        var givenUser = itEmployee();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(dhlEmployee(3)));
        when(assetRepository.findById(anyLong())).thenReturn(Optional.of(exampleAsset(99L)));
        when(mpkNumberRepository.findById(anyLong())).thenReturn(Optional.of(new MpkNumber(1L, "test")));
        when(orderRepository.findAllOrdersCreatedToday()).thenReturn(Collections.emptyList());
        when(dateUtil.getCurrentDate()).thenReturn(LocalDate.of(2022, 1, 1));

        orderService.addNewOrder(givenOrder, givenUser);

        verify(orderRepository).save(argThat(order -> order.getOrderNumber().equals("2022-01-01-1")));
        verify(mailService).sendNewOrder(any());
    }


    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDontExist() {
        var givenOrder = prepareOrderRequest();
        var givenUser = itEmployee();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> orderService.addNewOrder(givenOrder, givenUser));
    }

    @Test
    void shouldUpdateOrderWithNewStatus() {
        var givenOrderId = 99L;
        var givenStatus = Status.COMPLETED;

        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));

        orderService.changeStatus(givenOrderId, givenStatus, itEmployee());

        verify(orderRepository).save(argThat(order ->
                order.getStatus().equals(Status.COMPLETED) && order.getChangeHistory().size() != 0));
        verify(mailService).sendChangingStatusUpdate(any());
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderIsNotFound() {
        var givenOrderId = 99L;
        var givenStatus = Status.COMPLETED;
        var givenEmployee = itEmployee();

        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.changeStatus(givenOrderId, givenStatus, givenEmployee));
    }

    @Test
    void shouldAssignUserToOrder() {
        var givenOrderId = 99L;
        var givenUserId = 99L;
        var givenUser = itEmployee();

        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));
        when(userRepository.findById(99L)).thenReturn(Optional.of(givenUser));

        orderService.assignUserToOrder(givenOrderId, givenUserId, itEmployee());

        verify(orderRepository).save(
                argThat(order ->
                        order.getAssignmentDate() != null &&
                                order.getAssignedTo().equals(givenUser) &&
                                order.getChangeHistory().size() != 0));
        verify(mailService).sendChangingStatusUpdate(any());
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenAssigningToOrderThatDoesNotExist() {
        var givenOrderId = 99L;
        var givenUserId = 99L;
        var givenCallingUser = itEmployee();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.assignUserToOrder(givenOrderId, givenUserId, givenCallingUser));
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenAssigningOrderToUserThatDoesNotExist() {
        var givenOrderId = 99L;
        var givenUserId = 99L;
        var givenCallingUser = itEmployee();

        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> orderService.assignUserToOrder(givenOrderId, givenUserId, givenCallingUser));
    }

    @ParameterizedTest
    @MethodSource("usersThatAreNotItEmployees")
    void shouldThrowUserIsNotEmployeeItExceptionWhenAssigningOrderToUserThatIsNotItEmployee(User givenUser) {
        var givenOrderId = 99L;
        var givenUserId = 99L;
        var givenCallingUser = itEmployee();

        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));
        when(userRepository.findById(99L)).thenReturn(Optional.of(givenUser));

        assertThrows(UserIsNotEmployeeItException.class, () -> orderService.assignUserToOrder(givenOrderId, givenUserId, givenCallingUser));
    }

    @Test
    void shouldChangeRemarkWithAddedChangeHistory() {
        var givenOrderId = 99L;
        var givenRemark = "Test";
        var givenUser = dhlManager();

        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));

        orderService.addRemark(givenOrderId, givenRemark, givenUser);

        verify(orderRepository).save(argThat(order -> order.getChangeHistory().size() != 0));
    }

    @Test
    void shouldThrowUserUserNotPermittedExceptionWhenDhlEmployeeTriesToChangeNotTheirsOrderRemark() {
        var givenOrderId = 99L;
        var givenRemark = "Test";
        var givenUser = dhlEmployee(99);

        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));

        assertThrows(UserNotPermittedException.class, () -> orderService.addRemark(givenOrderId, givenRemark, givenUser));
    }

    private NewOrderRequest prepareOrderRequest() {
        return new NewOrderRequest("DHLemployee1@dhl.com", LocalDateTime.MAX, "test", 99L, Model.DESKTOP_PC, DeliveryType.PICKUP, "Test address");
    }

    private static Stream<User> usersThatAreNotItEmployees() {
        return Stream.of(
                itManager(),
                dhlManager(),
                dhlEmployee(3)
        );
    }

}
