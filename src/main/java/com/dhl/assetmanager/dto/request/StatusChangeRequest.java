package com.dhl.assetmanager.dto.request;

import com.dhl.assetmanager.entity.Status;
import com.dhl.assetmanager.validation.EnumValues;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class StatusChangeRequest implements Serializable {
    @EnumValues(enumClass = Status.class)
    private String newStatus;
}
