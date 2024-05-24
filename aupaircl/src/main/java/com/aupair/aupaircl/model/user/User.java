package com.aupair.aupaircl.model.user;

import com.aupair.aupaircl.model.rol.Rol;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id_user",nullable = false)
    private UUID userId;

    @Column(name = "username",nullable = false, unique = true)
    private String username;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name="password",nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Rol role;
    @Column(name = "email_verified")
    private Boolean emailVerified = false;
    @Column(name = "created_at",nullable = false)
    private Date createdAt;
    @Column(name = "last_login")
    private Date lastLogin;
    @Column(name="failed_attempts")
    private Integer failedAttempts = 0;
    @Column(name="is_locked")
    private Boolean isLocked = false;
    @Column(name = "reset_token")
    private String resetToken;
    @Column(name = "reset_token_expires")
    private Date resetTokenExpires;
    @PrePersist
    private void generateUUID(){
        if (this.userId==null){
            this.userId = UUID.randomUUID();
        }
    }
}

