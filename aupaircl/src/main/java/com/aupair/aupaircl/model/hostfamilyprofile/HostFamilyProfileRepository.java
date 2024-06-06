package com.aupair.aupaircl.model.hostfamilyprofile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HostFamilyProfileRepository extends JpaRepository<HostFamilyProfile, UUID> {
    Optional<HostFamilyProfile> findByUser_Email(String email);
    HostFamilyProfile findByUser_EmailAndUser_IsLocked(String email,boolean isLocked);
    }


