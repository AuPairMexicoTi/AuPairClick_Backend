package com.aupair.aupaircl.model.aupairprofile;

import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "apc_au_pair_profiles")
public class AuPairProfile {

    @Id
    @Column(name = "id_profile_aupair")
    @GeneratedValue(generator = "UUID")
    private UUID auPairProfileId;

    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(name = "available_from",nullable = false)
    private Date availableFrom;

    @Column(name = "available_to",nullable = false)
    private Date availableTo;
    @Column(name = "child_care_experience",nullable = false)
    private String childcareExperience;
    @Column(name = "motivation",nullable = false)
    private String motivation;
    @Column(name = "smoke")
    private Boolean smokes;
    @PrePersist
    private void generateUUID(){
        if(this.auPairProfileId==null){
            this.auPairProfileId= UUID.randomUUID();
        }
    }
}
