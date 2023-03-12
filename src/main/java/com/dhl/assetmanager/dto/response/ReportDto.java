package com.dhl.assetmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ReportDto implements Serializable {
    private final long id;
    private final UserDto generatedBy;
    private final LocalDateTime timestamp;
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;
}
