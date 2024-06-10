package com.aupair.aupaircl.model.hostfamilypreferredcountry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HostFamilyPreferredCountryRepository extends JpaRepository<HostFamilyPreferredCountry, UUID> {
    List<HostFamilyPreferredCountry> findByHostFamilyProfile_UserEmail(String user);

}
