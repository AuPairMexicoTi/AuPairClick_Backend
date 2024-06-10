package com.aupair.aupaircl.model.country;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_countries")
public class Country {

    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "id_country")
    @GeneratedValue(generator = "UUID")
    private UUID countryId;

    @Column(name = "country_name",nullable = false)
    private String countryName;

    @Column(name = "nationality",nullable = false)
    private String nationality;

    @Column(name = "country_code",nullable = false)
    private String countryCode;

    @PrePersist
    private void generateUUID(){
        if(this.countryId==null){
            this.countryId= UUID.randomUUID();
        }
    }
}

