package com.aupair.aupaircl.model.emailverification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailVerificationRepository  extends JpaRepository<EmailVerification, UUID> {
}
