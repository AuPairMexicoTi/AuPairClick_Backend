package com.aupair.aupaircl.model.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository  extends JpaRepository<Profile, UUID> {
Optional<Profile> findByUser_Email(String email);
boolean existsByUser_Email(String email);
}
