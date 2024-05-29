package com.aupair.aupaircl.model.rol;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RolRepository extends JpaRepository<Rol, UUID> {
Optional<Rol> findByRoleName(String roleName);
}
