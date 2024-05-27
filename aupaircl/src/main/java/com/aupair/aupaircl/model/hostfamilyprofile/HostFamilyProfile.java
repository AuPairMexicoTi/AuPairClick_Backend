package com.aupair.aupaircl.model.hostfamilyprofile;

import com.aupair.aupaircl.model.lada.Lada;
import com.aupair.aupaircl.model.locationtype.LocationTypes;
import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_host_family_profiles")
public class HostFamilyProfile {

    @Id
    @Column(name = "id_profile_host_family",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID hostFamilyProfileId;

    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(name = "number_of_childer",nullable = false)
    private Integer numberOfChildren;
    @Column(name = "children_age",nullable = false)
    private String childrenAges;
    @Column(name = "house_description",nullable = false)
    private String houseDescription;

    @ManyToOne
    @JoinColumn(name = "fk_location_type", nullable = false)
    private LocationTypes locationType;

    @Column(name = "search_from",nullable = false)
    private Date searchFrom;

    @Column(name = "search_to",nullable = false)
    private Date searchTo;
    @Column(name = "hosting_experience")
    private String hostingExperience;
    @Column(name = "smoke")
    private Boolean smokes;
    @OneToOne
    @JoinColumn(name = "fk_lada")
    private Lada lada;
    @PrePersist
    private void generateUUID(){
        if(this.hostFamilyProfileId==null){
            this.hostFamilyProfileId= UUID.randomUUID();
        }
    }
}
