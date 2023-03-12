package com.dhl.assetmanager.controller;

import com.dhl.assetmanager.entity.User;
import com.dhl.assetmanager.repository.AssetRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class AssetControllerTest extends BaseTestController {

    @MockBean
    private AssetRepository assetRepository;

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideUsersWithPermissions")
    void shouldReturn200WithAssetsForGivenUser(User givenUser) {
        var accessToken = getAccessToken(givenUser);

        when(assetRepository.findAllByCurrentUser_Id(anyLong()))
                .thenReturn(List.of(exampleAsset(99L), exampleAsset(999L)));

        mockMvc.perform(
                        get("/api/assets/users/" + givenUser.getId())
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideUsersWithoutPermissions")
    void shouldReturn403WhenUserIsNotPermittedWhenGettingAllAssets(User givenUser) {
        var accessToken = getAccessToken(givenUser);

        mockMvc.perform(
                        get("/api/assets/users/" + givenUser.getId())
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void shouldReturn403WhenUserWantsToGetSomeoneElseAssets() {
        var accessToken = getAccessToken(dhlEmployee(3));

        mockMvc.perform(
                        get("/api/assets/users/999")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideUsersWithPermissions")
    void shouldReturn200WithAssetDetails(User givenUser) {
        var accessToken = getAccessToken(givenUser);
        var givenAsset = exampleAssetWithCurrentUser(99L, givenUser);

        when(assetRepository.findById(anyLong())).thenReturn(Optional.of(givenAsset));

        mockMvc.perform(
                        get("/api/assets/99")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    void shouldReturn404WhenAssetIsNotFound() {
        var accessToken = getAccessToken(dhlEmployee(3));

        when(assetRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/api/assets/99")
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private Stream<User> provideUsersWithPermissions() {
        return Stream.of(
                dhlManager(),
                dhlEmployee(3)
        );
    }

    private Stream<User> provideUsersWithoutPermissions() {
        return Stream.of(
                itManager(),
                itEmployee()
        );
    }

}
