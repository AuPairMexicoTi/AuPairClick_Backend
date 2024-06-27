package com.aupair.aupaircl.model.lada;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LadaRepository extends JpaRepository<Lada, UUID> {
    Optional<Lada> findByLadaName(String name);
}
