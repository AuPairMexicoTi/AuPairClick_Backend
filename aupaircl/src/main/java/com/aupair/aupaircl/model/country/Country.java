package com.aupair.aupaircl.model.country;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
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

