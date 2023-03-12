package com.dhl.assetmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ChangeHistoryDto implements Serializable {
    private final LocalDateTime timestamp;
    private final String status;
    private final String remark;
    private final UserDto author;
}
