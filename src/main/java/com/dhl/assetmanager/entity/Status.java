package com.dhl.assetmanager.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    NEW("Nowy"),
    HANDED_FOR_COMPLETION("Przekazano do realizacji"),
    ALLOCATED("Przydzielono komputer"),
    IN_PREPARATION("W przygotowaniu"),
    SENT("Wysłany"),
    COMPLETED("Zakończone");

    private final String message;

}
