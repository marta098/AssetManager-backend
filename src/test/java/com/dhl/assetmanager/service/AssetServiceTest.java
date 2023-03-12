package com.dhl.assetmanager.service;

import com.dhl.assetmanager.dto.response.AssetDto;
import com.dhl.assetmanager.exception.AssetNotFoundException;
import com.dhl.assetmanager.exception.UserNotPermittedException;
import com.dhl.assetmanager.repository.AssetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static com.dhl.assetmanager.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class AssetServiceTest {

    @Autowired
    private AssetService assetService;

    @MockBean
    private AssetRepository assetRepository;

    @Test
    void shouldGetAssetDetailsForGivenId() {
        var givenId = 99L;
        var givenUser = dhlEmployee(3);
        var givenAsset = exampleAssetWithCurrentUser(99L, givenUser);

        when(assetRepository.findById(99L)).thenReturn(Optional.of(givenAsset));

        var actualAsset = assetService.getAsset(givenId);

        assertThat(actualAsset)
                .hasNoNullFieldsOrPropertiesExcept("mpkNumber", "createdBy");
    }

    @Test
    void shouldThrowAssetNotFoundExceptionWhenAssetIsNotFound() {
        var givenId = 99L;

        when(assetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AssetNotFoundException.class, () -> assetService.getAsset(givenId));
    }

    @Test
    void shouldReturnAllAssetsThatUserOwns() {
        var givenUserId = 3L;
        var givenUser = dhlEmployee(3);

        when(assetRepository.findAllByCurrentUser_Id(3L))
                .thenReturn(List.of(exampleAsset(99L), exampleAsset(999L)));

        var actualAssets = assetService.getAllUsersAssets(givenUserId, givenUser);

        assertThat(actualAssets)
                .hasSize(2)
                .hasOnlyElementsOfType(AssetDto.class);
    }

    @Test
    void shouldThrowUserNotPermittedExceptionWhenUserTriesToGetNotTheirsAssets() {
        var givenUserId = 99L;
        var givenUser = dhlEmployee(3);

        assertThrows(UserNotPermittedException.class, () -> assetService.getAllUsersAssets(givenUserId, givenUser));
    }

}
