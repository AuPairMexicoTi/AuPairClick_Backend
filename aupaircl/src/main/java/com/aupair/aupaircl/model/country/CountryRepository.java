package com.aupair.aupaircl.model.country;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<Country, UUID> {
Optional<Country> findByCountryName(String name);
}
