package com.aupair.aupaircl.model.notfication;


import com.aupair.aupaircl.model.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_notifications")
public class Notification {

    @Id
    @Column(name = "id_notification",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID notificationId;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(name = "message",nullable = false)
    private String message;
    @Column(name = "read_status",nullable = false)
    private boolean readStatus = false;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    @PrePersist
    private void generateUUID(){
        if(this.createdAt==null){
            this.createdAt= new Date();
        }
        if(this.notificationId==null){
            this.notificationId=UUID.randomUUID();
        }
    }
}
