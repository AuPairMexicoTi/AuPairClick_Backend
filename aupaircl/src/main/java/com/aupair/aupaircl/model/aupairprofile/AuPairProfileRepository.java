package com.aupair.aupaircl.model.aupairprofile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuPairProfileRepository extends JpaRepository<AuPairProfile, UUID> {
    Optional<AuPairProfile> findByUser_Email(String email);
    AuPairProfile findByUser_EmailAndIsApproved(String email,boolean isApp);

    @Query("SELECT DISTINCT hfp FROM AuPairProfile hfp " +
            "JOIN hfp.user u " +
            "JOIN hfp.preferredCountries pc " +
            "JOIN u.profile p " +
            "WHERE :familyCountry IN (SELECT c.country.countryName FROM hfp.preferredCountries c) " +
            "AND hfp.gender.genderName = :genderSearch " +
            "AND p.country.countryName IN :preferredCountryNames " +
            "AND hfp.availableFrom <= :endDate " +
            "AND hfp.availableTo >= :startDate " +
            "AND p.minStayMonths <= :maxDuration " +
            "AND p.maxStayMonths >= :minDuration")
    List<AuPairProfile> findAuPair(
            @Param("familyCountry") String familyCountry,
            @Param("genderSearch") String genderSearch,
            @Param("preferredCountryNames") List<String> preferredCountryNames,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("minDuration") int minDuration,
            @Param("maxDuration") int maxDuration
    );
}
