package com.aupair.aupaircl.model.aupairprofile;

import com.aupair.aupaircl.model.gender.Gender;
import com.aupair.aupaircl.model.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "apc_au_pair_profiles")
public class AuPairProfile {

    @Id
    @Column(name = "id_profile_aupair")
    @GeneratedValue(generator = "UUID")
    private UUID auPairProfileId;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    @Column(name = "available_from",nullable = false)
    private Date availableFrom;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    @Column(name = "available_to",nullable = false)
    private Date availableTo;
    @Column(name = "child_care_experience",nullable = false)
    private String childcareExperience;
    @Column(name = "motivation",nullable = false)
    private String motivation;
    @Column(name = "isApproved")
    private Boolean isApproved = false;
    @Column(name = "smoke")
    private Boolean smokes;
    @ManyToOne
    @JoinColumn(name = "fk_gender", nullable = false)
    private Gender gender;
    @PrePersist
    private void generateUUID(){
        if(this.auPairProfileId==null){
            this.auPairProfileId= UUID.randomUUID();
        }
    }
}
