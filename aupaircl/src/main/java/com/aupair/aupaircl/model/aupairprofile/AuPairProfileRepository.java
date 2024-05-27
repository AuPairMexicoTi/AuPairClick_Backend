package com.aupair.aupaircl.model.aupairprofile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuPairProfileRepository extends JpaRepository<AuPairProfile, UUID> {
}
