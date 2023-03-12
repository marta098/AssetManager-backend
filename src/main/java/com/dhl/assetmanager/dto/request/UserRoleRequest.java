package com.dhl.assetmanager.dto.request;

import com.dhl.assetmanager.validation.OneOf;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserRoleRequest implements Serializable {

    @OneOf({"ROLE_MANAGER_IT", "ROLE_EMPLOYEE_IT", "ROLE_MANAGER_DHL", "ROLE_EMPLOYEE_DHL"})
    private String role;

}