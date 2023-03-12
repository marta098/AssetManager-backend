package com.dhl.assetmanager.dto.request;

import com.dhl.assetmanager.entity.AssetType;
import com.dhl.assetmanager.entity.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewAssetRequest implements Serializable {
    @NotNull
    @NotBlank
    private final String serialNumber;
    @NotNull
    private final Model model;
    @NotNull
    private final AssetType type;
    @NotNull
    private final LocalDateTime deprecation;
    @NotNull
    private final long locationId;
    @NotNull
    private final String name;
}
