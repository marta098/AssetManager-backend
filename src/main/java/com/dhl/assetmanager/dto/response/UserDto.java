package com.dhl.assetmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class UserDto implements Serializable {
    private final long id;
    private final String username;
    private final String email;
    private final String role;
    private final Boolean isDeleted;
}
