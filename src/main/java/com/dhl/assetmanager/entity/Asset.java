package com.dhl.assetmanager.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String serialNumber;
    @Enumerated(EnumType.STRING)
    private Model model;
    @Enumerated(EnumType.STRING)
    private AssetType type;
    private LocalDateTime deprecation;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    private User currentUser;
    @Enumerated(EnumType.STRING)
    private AssetStatus status;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    private Location location;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    private Stockroom stockroom;
    private String crestCode;
    private String name;
    @OneToMany(mappedBy = "asset", cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST})
    private List<Order> orders;
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    private List<AssetChangeHistory> changeHistory;

}
