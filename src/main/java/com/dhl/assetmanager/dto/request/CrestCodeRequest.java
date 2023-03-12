package com.dhl.assetmanager.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class CrestCodeRequest implements Serializable {
    @NotNull
    @NotEmpty
    private String crestCode;
}
