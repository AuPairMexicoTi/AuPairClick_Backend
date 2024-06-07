package com.aupair.aupaircl.model.aupairpreferredcountry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuPairPreferredCountryRepository extends JpaRepository<AuPairPreferredCountry, UUID> {
List<AuPairPreferredCountry> findByAuPairProfile_User_Email(String user);

}
