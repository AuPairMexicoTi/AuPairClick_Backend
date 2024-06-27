package com.aupair.aupaircl.model.emailverification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository  extends JpaRepository<EmailVerification, UUID> {
    Optional<EmailVerification> findByUser_Email(String user);
}
