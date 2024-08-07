package com.aupair.aupaircl.model.user;

import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.rol.Rol;
import com.aupair.aupaircl.model.user_has_credits.UserHasCredits;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private boolean emailVerified = false;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at",nullable = false)
    private Date createdAt;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @Column(name="failed_attempts")
    private Integer failedAttempts = 0;
    @Column(name="is_locked")
    private boolean isLocked = false;
    @Column(name = "reset_token")
    private String resetToken;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "reset_token_expires")
    private Date resetTokenExpires;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY,targetEntity = Profile.class)
    private Profile profile;
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch =FetchType.LAZY,targetEntity = AuPairProfile.class)
    private AuPairProfile auPairProfile;
    @JsonIgnore
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch =FetchType.LAZY,targetEntity = HostFamilyProfile.class)
    private HostFamilyProfile hostFamilyProfile;
    @JsonIgnore
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch =FetchType.LAZY,targetEntity = UserHasCredits.class)
    private UserHasCredits userHasCredits;
    @PrePersist
    private void generateUUID(){
        this.lastLogin= LocalDateTime.now();
        if(this.createdAt==null){
            this.createdAt = new Date();
        }
        if (this.userId==null){
            this.userId = UUID.randomUUID();
        }
    }
}

