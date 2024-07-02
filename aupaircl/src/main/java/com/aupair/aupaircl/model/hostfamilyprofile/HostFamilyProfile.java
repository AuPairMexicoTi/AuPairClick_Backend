package com.aupair.aupaircl.model.hostfamilyprofile;

import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountry;
import com.aupair.aupaircl.model.lada.Lada;
import com.aupair.aupaircl.model.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_host_family_profiles")
public class HostFamilyProfile {

    @Id
    @Column(name = "id_profile_host_family", nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID hostFamilyProfileId;

    @JsonIgnore
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(name = "number_of_children", nullable = false)
    private Integer numberOfChildren;
    @Column(name = "children_age_min",nullable = false)
   private Integer childrenAgesMin;
   @Column(name = "children_age_max",nullable = false)
   private Integer childrenAgesMax;
    @Column(name = "house_description", nullable = false)
    private String houseDescription;
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "search_from", nullable = false)
    private Date searchFrom;
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "search_to", nullable = false)
    private Date searchTo;

    @Column(name = "hosting_experience")
    private String hostingExperience;

    @Column(name = "smoke")
    private boolean smokes;

    @Column(name = "isApproved")
    private boolean isApproved = false;

    @Column(name = "gender_preferred")
    private String genderPreferred;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_lada")
    private Lada lada;
    @JsonIgnore
    @OneToMany(mappedBy = "hostFamilyProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HostFamilyPreferredCountry> preferredCountries;

    @PrePersist
    private void generateUUID() {
        if (this.hostFamilyProfileId == null) {
            this.hostFamilyProfileId = UUID.randomUUID();
        }
    }
}
