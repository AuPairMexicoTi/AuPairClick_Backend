package com.aupair.aupaircl.service.hostfamilypreferredcountryservice;

import com.aupair.aupaircl.controller.familypreferredcountriescontroller.familypreferredcountriesdto.FamilyPreferredCountriesDTO;
import com.aupair.aupaircl.model.country.Country;
import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountry;
import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountryRepository;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import com.aupair.aupaircl.service.profileservice.mapperprofile.MapperProfile;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class HostFamilyPreferredCountryService {
private final HostFamilyPreferredCountryRepository hostFamilyPreferredCountryRepository;
    private final MapperProfile mapperProfile;
    private final HostFamilyProfileRepository hostFamilyProfileRepository;
    private final ProfileRepository profileRepository;
    @Autowired
public HostFamilyPreferredCountryService(HostFamilyPreferredCountryRepository hostFamily,
                                         MapperProfile mapperProfile,HostFamilyProfileRepository hostFamilyProfileRepository,
                                         ProfileRepository profileRepository){
    this.hostFamilyPreferredCountryRepository=hostFamily;
        this.mapperProfile = mapperProfile;
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
    this.profileRepository = profileRepository;
    }
 @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> updatePreferredCountriesFamily(FamilyPreferredCountriesDTO familyPreferredCountriesDTO){
    try {
        if(isProfileHostFamilyCompletely(familyPreferredCountriesDTO.getEmail())){

            List<Country> preferredCountries = mapperProfile.mapCountriesFromNames(familyPreferredCountriesDTO.getCountryDTOS());
            HostFamilyProfile hostFamilyProfile = this.hostFamilyProfileRepository.findByUser_EmailAndIsApproved(familyPreferredCountriesDTO.getEmail(), true);
            if(hostFamilyProfile==null){
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
            }
            List<HostFamilyPreferredCountry> currentPreferredCountries = this.hostFamilyPreferredCountryRepository.findByHostFamilyProfile_UserEmail(familyPreferredCountriesDTO.getEmail());
            List<Country> countriesToAdd = new ArrayList<>(preferredCountries);
            List<HostFamilyPreferredCountry> countriesToRemove = new ArrayList<>();

            for (HostFamilyPreferredCountry hostFamilyPreferredCountry : currentPreferredCountries) {
                if (!preferredCountries.contains(hostFamilyPreferredCountry.getCountry().getCountryName())) {
                    countriesToRemove.add(hostFamilyPreferredCountry);
                }else{
                    countriesToAdd.remove(hostFamilyPreferredCountry.getCountry());
                }
            }
            this.hostFamilyPreferredCountryRepository.deleteAll(countriesToRemove);
            for (Country countryAdd : countriesToAdd){
                HostFamilyPreferredCountry hostFamilyPreferredCountry= new HostFamilyPreferredCountry();
                hostFamilyPreferredCountry.setHostFamilyProfile(hostFamilyProfile);
                hostFamilyPreferredCountry.setCountry(countryAdd);
                this.hostFamilyPreferredCountryRepository.saveAndFlush(hostFamilyPreferredCountry);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Paises preferidos actualizados"));
        }else{
            log.error("Alguna seccion del perfil no ah sido aprovada");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Alguna seccion del perfil no ah sido aprovada"));
        }
    }catch (Exception e){
        log.error("Algo sucedio al actualizar los paises preferidos");
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal"));
    }
 }
    public boolean isProfileHostFamilyCompletely(String email) {
        Optional<HostFamilyProfile> hostFamilyProfile = hostFamilyProfileRepository.findByUser_Email(email);
        Optional<Profile> profile = profileRepository.findByUser_Email(email);
        boolean profileApproved = profile.isPresent() && profile.get().getIsApproved();
        boolean profileAupairApproved = hostFamilyProfile.get().getIsApproved();
        return profileApproved && profileAupairApproved;
    }
}
