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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String orderNumber;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private MpkNumber mpkNumber;
    @Enumerated(EnumType.STRING)
    private Model requestedModel;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime addedDate;
    private LocalDateTime assignmentDate;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private User assignedTo;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private User assignedBy;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private User requester;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private User receiver;
    private LocalDateTime pickupDate;
    private String deliveryAddress;
    private String remark;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST})
    private Asset asset;
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST})
    private List<ChangeHistory> changeHistory;
    private Boolean isDhlReminderSent;
    private Boolean isItReminderSent;

}
