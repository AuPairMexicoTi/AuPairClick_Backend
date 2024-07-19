package com.aupair.aupaircl.model.aupairprofile;

import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;
import com.aupair.aupaircl.model.gender.Gender;
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
    @OneToOne(targetEntity = User.class,optional = false)
    @JoinColumn(name = "fk_user",referencedColumnName = "id_user")
    private User user;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "available_from",nullable = false)
    private Date availableFrom;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "available_to",nullable = false)
    private Date availableTo;
    @Column(name = "isApproved")
    private boolean isApproved = false;
    @Column(name = "children_age_min_search")
    private Integer childrenAgeMinSearch;
    @Column(name = "children_age_max_search")
    private Integer childrenAgeMaxSearch;
    @Column(name = "smoker",nullable = false)
    private boolean smoker;
    @Column(name = "family_smokes",nullable = false)
    private boolean familySmokes;
    @Column(name="driving_licence",nullable = false)
    private boolean drivingLicence;
    @Column(name = "house_work",nullable = false)
    private boolean houseWork;
    @Column(name = "child_care_exp",nullable = false)
    private boolean childCareExp;
    @Column(name = "work_special_children",nullable = false)
    private boolean workSpecialChildren;
    @Column(name = "single_families",nullable = false)
    private boolean singleFamily;
    @Column(name = "vegetarian",nullable = false)
    private boolean vegetarian;
    @Column(name = "language_our")
    private String languageOur;
    @Column(name = "language_our_other")
    private String languageOurOther;
    @Column(name = "language_other")
    private String languageOther;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_gender", nullable = false)
    private Gender gender;
    @JsonIgnore
    @OneToMany(mappedBy = "auPairProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AuPairPreferredCountry> preferredCountries;

    @PrePersist
    private void generateUUID(){
        if(this.auPairProfileId==null){
            this.auPairProfileId= UUID.randomUUID();
        }
    }
}
