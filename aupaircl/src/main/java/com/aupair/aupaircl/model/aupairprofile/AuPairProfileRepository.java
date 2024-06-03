package com.aupair.aupaircl.model.aupairprofile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuPairProfileRepository extends JpaRepository<AuPairProfile, UUID> {
    Optional<AuPairProfile> findByUser_Email(String email);
}
