package com.aupair.aupaircl.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndIsLocked(String email,boolean isLocked);
    boolean existsByUsername(String username);
    Optional<User> findByProfile_NumPerfil(String numPerfil);
}
