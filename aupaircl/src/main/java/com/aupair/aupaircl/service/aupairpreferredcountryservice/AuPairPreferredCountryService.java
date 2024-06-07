package com.aupair.aupaircl.service.aupairpreferredcountryservice;

import com.aupair.aupaircl.controller.aupairpreferredcountriescontroller.aupairpreferredcountriesdto.AuPairPreferredCountriesDTO;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountryRepository;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.country.Country;
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
public class AuPairPreferredCountryService {
private static AuPairPreferredCountryRepository auPairPreferredCountryRepository;
    private final MapperProfile mapperProfile;
    private static AuPairProfileRepository auPairProfileRepository;

    @Autowired
public AuPairPreferredCountryService(AuPairPreferredCountryRepository auPairPreferredCountryRepository,
                                     MapperProfile mapperProfile,AuPairProfileRepository auPairProfile) {
    this.auPairPreferredCountryRepository = auPairPreferredCountryRepository;
        this.mapperProfile = mapperProfile;
        this.auPairProfileRepository = auPairProfile;
    }

@Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse>  aupairPreferredCountry(AuPairPreferredCountriesDTO auPairPreferredCountriesDTO){
    try {
        List<Country> preferredCountries = mapperProfile.mapCountriesFromNames(auPairPreferredCountriesDTO.getCountryDTOS());
        AuPairProfile auPairProfile = this.auPairProfileRepository.findByUser_EmailAndIsApproved(auPairPreferredCountriesDTO.getEmail(),false);

        for (Country country : preferredCountries) {
            AuPairPreferredCountry auPairPreferredCountry = new AuPairPreferredCountry();
            auPairPreferredCountry.setAuPairProfile(auPairProfile);
            auPairPreferredCountry.setCountry(country);
            this.auPairPreferredCountryRepository.save(auPairPreferredCountry);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Paises preferidos actualizados"));
    }
catch (Exception e){
        log.error("Error al actualizar countries au pair "+e.getMessage());
        return ResponseEntity.status(200).body(new CustomResponse(true,200,"Algo salio mal"));
    }
}

}
