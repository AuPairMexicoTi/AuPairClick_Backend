package com.aupair.aupaircl.model.profile;

import com.aupair.aupaircl.model.country.Country;
import com.aupair.aupaircl.model.locationtype.LocationTypes;
import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "apc_profiles")
public class Profile {

    @Id
    @Column(name = "id_profile",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID profileId;


    @OneToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "age",nullable = false)
    private Integer age;

    @Column(name = "languages_spoken",nullable = false)
    private String languagesSpoken;
    @Column(name = "about_me",nullable = false)
    private String aboutMe;
    @Column(name = "min_stay_months",nullable = false)
    private Integer minStayMonths;
    @Column(name = "max_stay_months",nullable = false)
    private Integer maxStayMonths;
    @Column(name = "isApproved")
    private Boolean isApproved = false;
    @ManyToOne
    @JoinColumn(name = "fk_location_type", nullable = false)
    private LocationTypes locationType;
    @ManyToOne
    @JoinColumn(name = "fk_country", nullable = false)
    private Country country;

    @PrePersist
    private void generateUUID(){
        if(this.profileId==null){
            this.profileId = UUID.randomUUID();
        }
    }
}
