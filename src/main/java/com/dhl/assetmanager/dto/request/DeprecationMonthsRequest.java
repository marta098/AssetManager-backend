package com.dhl.assetmanager.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class DeprecationMonthsRequest implements Serializable {
    @NotNull
    @Min(1)
    private int deprecationMonths;
}
