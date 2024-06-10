package com.aupair.aupaircl.model.country;

import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_countries")
public class Country {

    @Id
    @Column(name = "id_country")
    @GeneratedValue(generator = "UUID")
    private UUID countryId;

    @Column(name = "country_name",nullable = false)
    private String countryName;

    @Column(name = "nationality",nullable = false)
    private String nationality;

    @Column(name = "country_code",nullable = false)
    private String countryCode;
    @OneToMany(mappedBy = "country")
    private List<HostFamilyPreferredCountry> preferredCountries;

    @PrePersist
    private void generateUUID(){
        if(this.countryId==null){
            this.countryId= UUID.randomUUID();
        }
    }
}

