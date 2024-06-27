package com.aupair.aupaircl.model.stayhistory;

import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_stay_history")
public class StayHistory {

    @Id
    @Column(name = "id_stay",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID stayId;

    @ManyToOne
    @JoinColumn(name = "au_pair_id", nullable = false)
    private User auPair;

    @ManyToOne
    @JoinColumn(name = "host_family_id", nullable = false)
    private User hostFamily;

    @Column(name = "stay_start_date",nullable = false)
    private Date stayStartDate;

    @Column(name = "stay_end_date",nullable = false)
    private Date stayEndDate;
    @Column(name = "feedback")
    private String feedback;
    @PrePersist
    private void generateUUID(){
        if(this.stayId==null){
            stayId = UUID.randomUUID();
        }
    }
}

