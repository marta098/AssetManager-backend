package com.dhl.assetmanager.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
    private Asset asset;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
    private User owner;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
    private User author;
    private LocalDateTime timestamp;
    private LocalDateTime assignmentDate;
    private LocalDateTime dischargeDate;
    private LocalDateTime deprecation;
    private String remark;
    @Enumerated(EnumType.STRING)
    private AssetStatus status;

}
