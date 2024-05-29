package com.aupair.aupaircl.model.gender;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GenderRepository extends JpaRepository<Gender, UUID> {
    Optional<Gender> findByGenderName (String gender);
}
