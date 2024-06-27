package com.aupair.aupaircl.model.availabilitystatus;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "apc_availability_status")
public class AvailabilityStatus {

    @Id
    @Column(name = "id_status",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID statusId;

    @Column(name = "status_name",nullable = false, unique = true)
    private String statusName;
    @PrePersist
    private void generateUUID(){
        if(statusId==null){
            this.statusId= UUID.randomUUID();
        }
    }
}
