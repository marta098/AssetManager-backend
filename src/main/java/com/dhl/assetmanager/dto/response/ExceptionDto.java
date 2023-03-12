package com.dhl.assetmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ExceptionDto {
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
