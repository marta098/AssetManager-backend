package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.response.OrderDto;
import com.dhl.assetmanager.entity.Role;
import com.dhl.assetmanager.repository.OrderRepository;
import com.dhl.assetmanager.repository.RoleRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static com.dhl.assetmanager.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends BaseTestController {

    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private OrderRepository orderRepository;

    @SneakyThrows
    @Test
    void shouldReturnUserDtoListForGivenRole() {
        var accessToken = getAccessToken(itEmployee());
        var givenRole = new Role(99L, "ROLE_EMPLOYEE_IT");

        when(roleRepository.findByName("ROLE_EMPLOYEE_IT")).thenReturn(Optional.of(givenRole));
        when(userRepository.findAllByRole(givenRole)).thenReturn(List.of(itEmployee(), itEmployee()));

        mockMvc.perform(
                        get("/api/users/role_employee_it")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void shouldReturn400When() {
        var accessToken = getAccessToken(itEmployee());

        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/api/users/wrong_role")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldReturn200WithOrders() {
        var accessToken = getAccessToken(dhlEmployee(99L));

        when(orderRepository.findAllByReceiver_IdOrderByAddedDateDesc(99L)).thenReturn(List.of(exampleOrder(1L)));

        var result = mockMvc.perform(
                        get("/api/users/99/orders")
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

}
