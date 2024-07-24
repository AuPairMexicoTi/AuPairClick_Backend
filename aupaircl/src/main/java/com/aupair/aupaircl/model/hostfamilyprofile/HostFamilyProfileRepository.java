package com.aupair.aupaircl.model.hostfamilyprofile;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HostFamilyProfileRepository extends JpaRepository<HostFamilyProfile, UUID> {

    Optional<HostFamilyProfile> findByUser_Email(String email);

    HostFamilyProfile findByUser_EmailAndUser_IsLocked(String email, boolean isLocked);
    HostFamilyProfile findByUser_EmailAndIsApproved(String email, boolean isApproved);
    int countHostFamilyProfileByIsApproved(boolean isApproved);
    @EntityGraph(attributePaths = {"user", "preferredCountries", "user.profile"})
    @Query("SELECT DISTINCT hfp FROM HostFamilyProfile hfp " +
            "JOIN hfp.user u " +
            "JOIN hfp.preferredCountries pc " +
            "JOIN u.profile p " +
            "WHERE :auPairCountry IN (SELECT c.country.countryName FROM hfp.preferredCountries c) " +
            "AND hfp.genderPreferred = :gender " +
            "AND p.country.countryName IN :preferredCountryIds " +
            "AND hfp.searchFrom <= :endDate " +
            "AND hfp.searchTo >= :startDate " +
            "AND p.minStayMonths <= :maxDuration " +
            "AND p.maxStayMonths >= :minDuration")
    List<HostFamilyProfile> findHostFamilies(
            @Param("auPairCountry") String auPairCountry,
            @Param("gender") String gender,
            @Param("preferredCountryIds") List<String> preferredCountryIds,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("minDuration") int minDuration,
            @Param("maxDuration") int maxDuration
    );
    @Query("SELECT DISTINCT hfp FROM HostFamilyProfile hfp " +
            "JOIN hfp.user u " +
            "JOIN hfp.preferredCountries pc " +
            "JOIN u.profile p " +
            "WHERE :auPairCountry IN (SELECT c.country.countryName FROM hfp.preferredCountries c) " +
            "AND hfp.genderPreferred = :gender " +
            "AND p.locationType.locationTypeName = :locationType "+
            "AND p.country.countryName IN :preferredCountryIds " +
            "AND hfp.searchFrom <= :endDate " +
            "AND hfp.searchTo >= :startDate " +
            "AND p.minStayMonths <= :maxDuration " +
            "AND p.maxStayMonths >= :minDuration " +
            "AND hfp.childrenAgesMin <= :childrenAgesMax " +
            "AND hfp.childrenAgesMax >= :childrenAgesMin " +
            "AND hfp.aupairHouseWork = :aupairHouseWork " +
            "AND hfp.areSingleFamily = :areSingleFamily " +
            "AND hfp.smokesInFamily = :smokesInFamily " +
            "AND hfp.aupairCareChildrenNeed = :aupairCareChildrenNeed")
    List<HostFamilyProfile> findHostFamiliesDashboard(
            @Param("auPairCountry") String auPairCountry,
            @Param("gender") String gender,
            @Param("locationType") String locationType,
            @Param("preferredCountryIds") List<String> preferredCountryIds,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("minDuration") int minDuration,
            @Param("maxDuration") int maxDuration,
            @Param("childrenAgesMax") int childrenAgesMin,
            @Param("childrenAgesMin") int childrenAgesMax,
            @Param("aupairHouseWork") boolean aupairHouseWork,
            @Param("areSingleFamily") boolean areSingleFamily,
            @Param("smokesInFamily") boolean smokesInFamily,
            @Param("aupairCareChildrenNeed") boolean aupairCareChildrenNeed
    )  ;
}
