package com.dhl.assetmanager.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RoleNotFoundException extends RuntimeException {

    private final boolean isProvidedByUser;

}
