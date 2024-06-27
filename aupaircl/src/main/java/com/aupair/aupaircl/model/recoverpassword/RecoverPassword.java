package com.aupair.aupaircl.model.recoverpassword;

import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "apc_recover_password")
public class RecoverPassword {
    @Id
    @Column(name = "id:recover_passsword",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID recoverPassswordId;
    @ManyToOne
    @JoinColumn(name = "fk_user",nullable = false)
    private User user;
    @Column(name = "verification_password",nullable = false)
    private String verificationToken;
    @Column(name = "is_verification",nullable = false)
    private Boolean isVerification = false;
    @Column(name = "expires_at",nullable = false)
    private Date expiresAt;
    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    @PrePersist
    private void generateUUID(){
        if(this.createdAt==null){
            this.createdAt= new Date();
        }
        if(this.recoverPassswordId==null){
            this.recoverPassswordId= UUID.randomUUID();
        }
    }
}
