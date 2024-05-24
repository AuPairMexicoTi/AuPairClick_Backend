package com.aupair.aupaircl.model.hostfamilypreferredcountry;

import com.aupair.aupaircl.model.country.Country;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_host_family_preferred_countries")
public class HostFamilyPreferredCountry {

    @Id
    @Column(name = "id_host_family_preferred_countries")
    @GeneratedValue(generator = "UUID")
    private UUID idHostFamilyPreferredCountries;

    @ManyToOne
    @JoinColumn(name = "fk_host_family_profile", nullable = false)
    private HostFamilyProfile hostFamilyProfile;

    @ManyToOne
    @JoinColumn(name = "fk_country", nullable = false)
    private Country country;
    @PrePersist
    private void generateUUID(){
        if(this.idHostFamilyPreferredCountries==null){
            this.idHostFamilyPreferredCountries=UUID.randomUUID();
        }
    }
}

