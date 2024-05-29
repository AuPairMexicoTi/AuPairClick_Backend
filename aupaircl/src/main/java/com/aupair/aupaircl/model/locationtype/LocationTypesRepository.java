package com.aupair.aupaircl.model.locationtype;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocationTypesRepository extends JpaRepository<LocationTypes, UUID> {
Optional<LocationTypes> findByLocationTypeName(String name);
}
