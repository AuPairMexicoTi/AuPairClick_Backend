package com.aupair.aupaircl.model.emailverification;

import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
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
@Table(name = "apc_email_verification")
public class EmailVerification {

    @Id
    @Column(name = "id_verification",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID verificationId;

    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(nullable = false)
    private String verificationToken;

    private Date expiresAt;
    private Date createdAt;
    @PrePersist
    private void generateUUID(){
        if(this.createdAt==null){
            this.createdAt= new Date();
        }
        if(this.verificationId==null){
            this.verificationId= UUID.randomUUID();
        }
    }
}
