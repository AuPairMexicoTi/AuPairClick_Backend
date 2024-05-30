package com.aupair.aupaircl.model.locationtype;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_location_types")
public class LocationTypes {


    @Id
    @Column(name = "id_location_type",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID locationTypeId;
    @Column(name = "location_type_name",nullable = false, unique = true)
    private String locationTypeName;
    @PrePersist
    private void generateUUID(){
        if(this.locationTypeId==null){
            this.locationTypeId= UUID.randomUUID();
        }
    }
}
