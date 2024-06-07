package com.aupair.aupaircl.service.hostfamilypreferredcountryservice;

import com.aupair.aupaircl.controller.familypreferredcountriescontroller.familypreferredcountriesdto.FamilyPreferredCountriesDTO;
import com.aupair.aupaircl.model.country.Country;
import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountry;
import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountryRepository;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.service.profileservice.mapperprofile.MapperProfile;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
@Transactional
public class HostFamilyPreferredCountryService {
private final HostFamilyPreferredCountryRepository hostFamilyPreferredCountryRepository;
    private final MapperProfile mapperProfile;
    private final HostFamilyProfileRepository hostFamilyProfileRepository;
    @Autowired
public HostFamilyPreferredCountryService(HostFamilyPreferredCountryRepository hostFamily,
                                         MapperProfile mapperProfile,HostFamilyProfileRepository hostFamilyProfileRepository){
    this.hostFamilyPreferredCountryRepository=hostFamily;
        this.mapperProfile = mapperProfile;
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
    }
 @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> updatePreferredCountriesFamily(FamilyPreferredCountriesDTO familyPreferredCountriesDTO){
    try {
        List<Country> preferredCountries = mapperProfile.mapCountriesFromNames(familyPreferredCountriesDTO.getCountryDTOS());
        HostFamilyProfile hostFamilyProfile = this.hostFamilyProfileRepository.findByUser_EmailAndUser_IsLocked(familyPreferredCountriesDTO.getEmail(), false);

        for (Country country : preferredCountries) {
            HostFamilyPreferredCountry auPairPreferredCountry = new HostFamilyPreferredCountry();
            auPairPreferredCountry.setHostFamilyProfile(hostFamilyProfile);
            auPairPreferredCountry.setCountry(country);
            this.hostFamilyPreferredCountryRepository.save(auPairPreferredCountry);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Paises preferidos actualizados"));
    }catch (Exception e){
        log.error("Algo sucedio al actualizar los paises preferidos");
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal"));
    }
 }
}
