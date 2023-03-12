package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.dto.request.UserCredentialsRequest;
import com.dhl.assetmanager.dto.response.TokenResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static com.dhl.assetmanager.TestData.itManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest extends BaseTestController {

    @SneakyThrows
    @Test
    void shouldReturn200WhenCredentialsAreCorrect() {
        when(userDetailsService.loadUserByUsername("test"))
                .thenReturn(itManager());

        var result = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new UserCredentialsRequest("test", "qwerty123"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        var token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenResponse.class);
        assertThat(token.getAccessToken())
                .isNotBlank();
    }

    @SneakyThrows
    @Test
    void shouldReturn401WhenBadCredentials() {
        when(userDetailsService.loadUserByUsername("test"))
                .thenReturn(itManager());

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new UserCredentialsRequest("test", "wrong_password"))))
                .andExpect(status().isUnauthorized());
    }

}
