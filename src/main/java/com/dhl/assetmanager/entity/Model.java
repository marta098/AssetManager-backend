package com.dhl.assetmanager.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Model {
    LARGE_LAPTOP("Duży laptop"),
    SMALL_LAPTOP("Mały laptop"),
    DESKTOP_PC("Komputer stacjonarny");

    private final String message;
}
