package com.aupair.aupaircl.model.user_has_credits;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserHasCreditsRepository extends JpaRepository<UserHasCredits,UUID> {
    Optional<UserHasCredits> findByUser_Email(String email);
}
