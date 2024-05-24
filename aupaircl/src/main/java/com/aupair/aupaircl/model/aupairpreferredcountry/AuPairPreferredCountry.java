package com.aupair.aupaircl.model.aupairpreferredcountry;

import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.country.Country;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Column;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_au_pair_preferred_country")
public class AuPairPreferredCountry {

    @Id
    @Column(name = "id_aupair_preferred_countries")
    @GeneratedValue(generator = "UUID")
    private UUID idAuPairPreferredCountries;

    @ManyToOne
    @JoinColumn(name = "fk_au_pair_profile", nullable = false)
    private AuPairProfile auPairProfile;

    @ManyToOne
    @JoinColumn(name = "fk_country", nullable = false)
    private Country country;
    @PrePersist
    private void generateUUID(){
        if(this.idAuPairPreferredCountries==null){
            this.idAuPairPreferredCountries=UUID.randomUUID();
        }
    }

}
