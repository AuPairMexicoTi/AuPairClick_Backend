package com.aupair.aupaircl.model.lada;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LadaRepository extends JpaRepository<Lada, UUID> {
}
