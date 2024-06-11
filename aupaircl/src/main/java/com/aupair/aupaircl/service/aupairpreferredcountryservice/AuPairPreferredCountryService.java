package com.aupair.aupaircl.service.aupairpreferredcountryservice;

import com.aupair.aupaircl.controller.aupairpreferredcountriescontroller.aupairpreferredcountriesdto.AuPairPreferredCountriesDTO;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountryRepository;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.country.Country;
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
public class AuPairPreferredCountryService {
private static AuPairPreferredCountryRepository auPairPreferredCountryRepository;
    private final MapperProfile mapperProfile;
    private static AuPairProfileRepository auPairProfileRepository;
    private static ProfileRepository profileRepository;
    @Autowired
public AuPairPreferredCountryService(AuPairPreferredCountryRepository auPairPreferredCountryRepository,
                                     MapperProfile mapperProfile,AuPairProfileRepository auPairProfile,ProfileRepository profileRepository) {
    this.auPairPreferredCountryRepository = auPairPreferredCountryRepository;
        this.mapperProfile = mapperProfile;
        this.auPairProfileRepository = auPairProfile;
        this.profileRepository = profileRepository;
    }
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> aupairPreferredCountry(AuPairPreferredCountriesDTO auPairPreferredCountriesDTO) {
        try {
            if (isProfileAuPairCompletely(auPairPreferredCountriesDTO.getEmail())) {
                // Mapear los nombres de países a entidades Country
                List<Country> preferredCountries = mapperProfile.mapCountriesFromNames(auPairPreferredCountriesDTO.getCountryDTOS());

                // Encontrar el perfil de AuPair
                AuPairProfile auPairProfile = this.auPairProfileRepository.findByUser_EmailAndIsApproved(auPairPreferredCountriesDTO.getEmail(), true);

                // Verificar si el perfil de AuPair existe
                if (auPairProfile == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse(true, HttpStatus.NOT_FOUND.value(), "Perfil de AuPair no encontrado"));
                }

                // Obtener las preferencias actuales de países
                List<AuPairPreferredCountry> currentPreferredCountries = this.auPairPreferredCountryRepository.findByAuPairProfile_User_Email(auPairProfile.getUser().getEmail());

                // Crear listas para identificar los cambios
                List<Country> countriesToAdd = new ArrayList<>(preferredCountries);
                List<AuPairPreferredCountry> countriesToRemove = new ArrayList<>();

                // Comparar las preferencias actuales con las nuevas
                for (AuPairPreferredCountry current : currentPreferredCountries) {
                    if (!preferredCountries.contains(current.getCountry().getCountryName())) {
                        countriesToRemove.add(current);
                    } else {
                        countriesToAdd.remove(current.getCountry());
                    }
                }

                // Eliminar las preferencias que ya no están presentes
                    this.auPairPreferredCountryRepository.deleteAll(countriesToRemove);

                // Agregar las nuevas preferencias que no existen en las actuales
                for (Country countryToAdd : countriesToAdd) {
                    AuPairPreferredCountry auPairPreferredCountry = new AuPairPreferredCountry();
                    auPairPreferredCountry.setAuPairProfile(auPairProfile);
                    auPairPreferredCountry.setCountry(countryToAdd);
                    this.auPairPreferredCountryRepository.saveAndFlush(auPairPreferredCountry);
                }
            }else {
                log.error("Alguna seccion del perfil no ah sido aprovada");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Alguna seccion del perfil no ah sido aprovada"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Paises preferidos actualizados"));
        } catch (Exception e) {
            log.error("Error al actualizar countries au pair " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salió mal"));
        }
    }

    public boolean isProfileAuPairCompletely(String email){
        Optional<AuPairProfile> auPairProfile = auPairProfileRepository.findByUser_Email(email);
        Optional<Profile> profile = profileRepository.findByUser_Email(email);
        boolean profileApproved = profile.isPresent() && profile.get().getIsApproved();
        boolean profileAupairApproved = auPairProfile.get().getIsApproved();
        return profileApproved && profileAupairApproved;
    }
}
