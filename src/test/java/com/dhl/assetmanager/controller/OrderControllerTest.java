package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.request.RemarkChangeRequest;
import com.dhl.assetmanager.dto.request.StatusChangeRequest;
import com.dhl.assetmanager.dto.response.OrderDto;
import com.dhl.assetmanager.repository.OrderRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.dhl.assetmanager.TestData.*;
import static com.dhl.assetmanager.entity.Status.COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest extends BaseTestController {

    @MockBean
    private OrderRepository orderRepository;

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideCorrectAccessTokensForGettingAllOrders")
    void shouldGetAllOrdersWhenUserHasCorrectRole(String accessToken) {
        when(orderRepository.findByOrderByAddedDateDesc()).thenReturn(
                List.of(exampleOrder(99L)));

        var result = mockMvc.perform(
                        get("/api/orders")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<OrderDto> orders = objectMapper.readValue(result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDto.class));

        assertThat(orders)
                .hasSize(1);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideIncorrectAccessTokensForGettingAllOrders")
    void shouldReturn403WhenUserIsNotPermitted(String accessToken) {
        mockMvc.perform(
                        get("/api/orders")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    void shouldReturn201WhenOrderStatusIsChanged() {
        var accessToken = getAccessToken(itEmployee());
        var givenRequest = new StatusChangeRequest();
        givenRequest.setNewStatus(COMPLETED.toString());

        when(orderRepository.findById(999L)).thenReturn(Optional.of(exampleOrder(999L)));

        mockMvc.perform(
                        put("/api/orders/999/status")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(givenRequest)))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void shouldReturn400WhenGivenStatusIsNotInEnumValues() {
        var accessToken = getAccessToken(itEmployee());
        var givenRequest = new StatusChangeRequest();
        givenRequest.setNewStatus("NOT_VALID_STATUS");

        mockMvc.perform(
                        put("/api/orders/999/status")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(givenRequest)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldReturn400WhenOrderIsNotFound() {
        var accessToken = getAccessToken(itEmployee());
        var givenRequest = new StatusChangeRequest();
        givenRequest.setNewStatus(COMPLETED.toString());

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(
                        put("/api/orders/999/status")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(givenRequest)))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void shouldReturn201WhenAssigningOrderToUser() {
        var accessToken = getAccessToken(itEmployee());
        var givenUser = itEmployee();

        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));
        when(userRepository.findById(99L)).thenReturn(Optional.of(givenUser));

        mockMvc.perform(
                        put("/api/orders/99/assign/99")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void shouldReturn404WhenAssigningOrderThatIsNotFound() {
        var accessToken = getAccessToken(itEmployee());

        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(
                        put("/api/orders/99/assign/99")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void shouldReturn404WhenAssigningOrderToUserThatIsNotFound() {
        var accessToken = getAccessToken(itEmployee());

        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(
                        put("/api/orders/99/assign/99")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void shouldReturn400WhenAssigningOrderToUserThatIsNotItEmployee() {
        var accessToken = getAccessToken(itEmployee());

        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));
        when(userRepository.findById(99L)).thenReturn(Optional.of(dhlEmployee(3)));

        mockMvc.perform(
                        put("/api/orders/99/assign/99")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldReturn201WhenChangingRemark() {
        var accessToken = getAccessToken(itEmployee());
        var givenRequest = new RemarkChangeRequest("test");

        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));

        mockMvc.perform(
                        put("/api/orders/99/remark")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(givenRequest)))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void shouldReturn403WhenChangingRemarkOfAnotherUser() {
        var accessToken = getAccessToken(dhlEmployee(99));
        var givenRequest = new RemarkChangeRequest("test");

        when(userRepository.findByUsername("DhlEmployee")).thenReturn(Optional.of(dhlEmployee(99)));
        when(orderRepository.findById(99L)).thenReturn(Optional.of(exampleOrder(99L)));

        mockMvc.perform(
                        put("/api/orders/99/remark")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(givenRequest)))
                .andExpect(status().isForbidden());
    }

    private Stream<String> provideCorrectAccessTokensForGettingAllOrders() {
        return Stream.of(
                getAccessToken(itEmployee())
        );
    }

    private Stream<String> provideIncorrectAccessTokensForGettingAllOrders() {
        return Stream.of(
                getAccessToken(itManager()),
                getAccessToken(dhlEmployee(3))
        );
    }
}
