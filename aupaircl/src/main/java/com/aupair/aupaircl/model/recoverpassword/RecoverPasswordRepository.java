package com.aupair.aupaircl.model.recoverpassword;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RecoverPasswordRepository extends JpaRepository<RecoverPassword, UUID> {
    Optional<RecoverPassword> findByUser_Email(String email);
}
