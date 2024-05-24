package com.aupair.aupaircl.model.gender;

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
@Table(name = "apc_genders")
public class Gender {

    @Id
    @Column(name = "id_gender",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID genderId;

    @Column(name = "gender_name",nullable = false, unique = true)
    private String genderName;
    @PrePersist
    private void generateUUID(){
        if(this.genderId==null){
            this.genderId=UUID.randomUUID();
        }
    }
}


