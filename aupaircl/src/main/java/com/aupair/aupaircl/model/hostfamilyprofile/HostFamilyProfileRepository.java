package com.aupair.aupaircl.model.hostfamilyprofile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HostFamilyProfileRepository extends JpaRepository<HostFamilyProfile, UUID> {
}