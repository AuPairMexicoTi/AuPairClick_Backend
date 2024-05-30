package com.aupair.aupaircl.service.profileservice.mapperprofile;

import com.aupair.aupaircl.controller.profilecontroller.profiledto.ProfileDTO;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.country.CountryRepository;
import org.springframework.stereotype.Service;

@Service
public class MapperProfile {
private static CountryRepository countryRepository;
public MapperProfile(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
}
public static void registerProfileAuPair(ProfileDTO profileDTO, AuPairProfile auPairProfile) {

}
}
