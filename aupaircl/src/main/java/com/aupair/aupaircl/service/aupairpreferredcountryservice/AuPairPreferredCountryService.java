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
import java.util.ArrayList;
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
    public ResponseEntity<CustomResponse> aupairPreferredCountry(AuPairPreferredCountriesDTO auPairPreferredCountriesDTO) {
        try {
            // Mapear los nombres de países a entidades Country
            List<Country> preferredCountries = mapperProfile.mapCountriesFromNames(auPairPreferredCountriesDTO.getCountryDTOS());

            // Encontrar el perfil de AuPair
            AuPairProfile auPairProfile = this.auPairProfileRepository.findByUser_EmailAndIsApproved(auPairPreferredCountriesDTO.getEmail(), false);

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
            for (AuPairPreferredCountry toRemove : countriesToRemove) {
                this.auPairPreferredCountryRepository.delete(toRemove);
            }

            // Agregar las nuevas preferencias que no existen en las actuales
            for (Country countryToAdd : countriesToAdd) {
                AuPairPreferredCountry auPairPreferredCountry = new AuPairPreferredCountry();
                auPairPreferredCountry.setAuPairProfile(auPairProfile);
                auPairPreferredCountry.setCountry(countryToAdd);
                this.auPairPreferredCountryRepository.saveAndFlush(auPairPreferredCountry);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Paises preferidos actualizados"));
        } catch (Exception e) {
            log.error("Error al actualizar countries au pair " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salió mal"));
        }
    }


}
