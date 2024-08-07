package com.aupair.aupaircl.model.user_has_subscription;

import com.aupair.aupaircl.model.subscription.Subscription;
import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_user_has_subscription")
public class UserHasSubscription {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id_user_has_subscription")
    private UUID id_user_has_subscription;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "fk_subscription", nullable = false)
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @PrePersist
    private void generateUUID(){
        if(this.id_user_has_subscription==null){
            this.id_user_has_subscription = UUID.randomUUID();
        }
        if(this.createdAt==null){
            this.createdAt = LocalDateTime.now();
        }
    }
}
