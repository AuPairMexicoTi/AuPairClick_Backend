package com.aupair.aupaircl.model.rol;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_roles")
public class Rol {

    @Id
    @Column(name = "id_role",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID roleId;

    @Column(name = "role_name",nullable = false, unique = true)
    private String roleName;

    @PrePersist
    private void generateUUID(){
        if(this.roleId==null){
            this.roleId = UUID.randomUUID();
        }
    }
}

