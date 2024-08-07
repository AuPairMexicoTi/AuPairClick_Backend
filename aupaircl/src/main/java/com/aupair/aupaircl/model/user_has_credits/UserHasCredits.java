package com.aupair.aupaircl.model.user_has_credits;

import com.aupair.aupaircl.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_user_has_credits")
public class UserHasCredits {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id_user_has_credits")
    private UUID IdUserHasCredits;
    @JsonIgnore
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;
    @Column(name = "current_credits")
    private Integer currentCredits;
}
