package com.dhl.assetmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] file;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private User generatedBy;
    private LocalDateTime timestamp;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

}
