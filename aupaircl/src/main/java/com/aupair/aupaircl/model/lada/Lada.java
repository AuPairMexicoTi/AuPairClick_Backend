package com.aupair.aupaircl.model.lada;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_lada")
public class Lada {
    @Id
    @Column(name = "id_lada",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID idLada;
    @Column(name = "lada_name")
    private String ladaName;
    @PrePersist
    private void generateUUID(){
        if(this.idLada==null){
            this.idLada=UUID.randomUUID();
        }
    }
}
