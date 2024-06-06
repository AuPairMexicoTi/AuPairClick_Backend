package com.aupair.aupaircl.service.hostfamilyprofileservice;

import com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto.FamilyProfileUpdateDTO;
import com.aupair.aupaircl.model.country.Country;
import com.aupair.aupaircl.model.gender.Gender;
import com.aupair.aupaircl.model.gender.GenderRepository;
import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountry;
import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountryRepository;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.lada.Lada;
import com.aupair.aupaircl.model.lada.LadaRepository;
import com.aupair.aupaircl.model.locationtype.LocationTypes;
import com.aupair.aupaircl.model.locationtype.LocationTypesRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.service.profileservice.mapperprofile.MapperProfile;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class HostFamilyProfileService {
    private final HostFamilyProfileRepository hostFamilyProfileRepository;
    private final HostFamilyPreferredCountryRepository hostFamilyPreferredCountry;
    private final MapperProfile mapperProfile;
    private final UserRepository userRepository;
    private final LocationTypesRepository locationTypesRepository;
    private final LadaRepository ladaRepository;
    private final GenderRepository genderRepository;
    public HostFamilyProfileService(HostFamilyProfileRepository hostFamilyProfileRepository,
                                    HostFamilyPreferredCountryRepository hostFamilyPreferredCountryRepository,
                                    MapperProfile mapperProfile, UserRepository userRepository,
                                    LocationTypesRepository locationTypesRepository,
                                    LadaRepository ladaRepository,GenderRepository genderRepository) {
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
        this.hostFamilyPreferredCountry = hostFamilyPreferredCountryRepository;
        this.mapperProfile = mapperProfile;
        this.userRepository = userRepository;
        this.locationTypesRepository = locationTypesRepository;
        this.ladaRepository = ladaRepository;
        this.genderRepository = genderRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getHostFamilyProfile(String email) {
        try {
            HostFamilyProfile hostFamilyProfile = this.hostFamilyProfileRepository.findByUser_EmailAndUser_IsLocked(email,false);
            if (hostFamilyProfile != null) {

                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Perfil de Familia",HttpStatus.OK.value(), false,hostFamilyProfile));
            }else{
                log.error("Family profile not found");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.BAD_REQUEST.value(), "Perfil no encontrado"));
            }
        } catch (Exception e) {
            log.error("Error en getHostProfile" + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal"));
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> updateHostFamilyProfile(FamilyProfileUpdateDTO familyProfileUpdateDTO) {
        try {
            Optional<User> userSaved = this.userRepository.findByEmail(familyProfileUpdateDTO.getEmail());
            if (userSaved.isEmpty()){
                log.error("Could not find user");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
            }
            if (Boolean.FALSE.equals(userSaved.get().getEmailVerified())) {
                log.error("Email isnt verified");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no verificado"));
            }
            Optional<LocationTypes> locationTypeSaved = this.locationTypesRepository.findByLocationTypeName(familyProfileUpdateDTO.getLocation_type());
            if (locationTypeSaved.isEmpty()) {
                log.error("Could not find location type");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Location invalida"));
            }
            Optional<Lada> ladaSaved = this.ladaRepository.findByLadaName(familyProfileUpdateDTO.getLada());
            if(ladaSaved.isEmpty()){
                log.error("Could not find lada");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Lada invalida"));
            }
            Optional<Gender> genderSaved = this.genderRepository.findByGenderName(familyProfileUpdateDTO.getGenderPreferred());
            if(genderSaved.isEmpty()){
                log.error("Could not find gender");
                 return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Genero invalido"));
            }
            HostFamilyProfile hostFamilyProfile = new HostFamilyProfile();
            hostFamilyProfile.setHostingExperience(familyProfileUpdateDTO.getHostin_experience());
            hostFamilyProfile.setHouseDescription(familyProfileUpdateDTO.getHouse_description());
            hostFamilyProfile.setChildrenAges(familyProfileUpdateDTO.getChildren_Age());
            hostFamilyProfile.setNumberOfChildren(familyProfileUpdateDTO.getNumber_of_children());
            hostFamilyProfile.setSearchFrom(familyProfileUpdateDTO.getSearch_from());
            hostFamilyProfile.setSearchTo(familyProfileUpdateDTO.getSearch_to());
            hostFamilyProfile.setLada(ladaSaved.get());
            hostFamilyProfile.setSmokes(familyProfileUpdateDTO.getSmokes());
            hostFamilyProfile.setUser(userSaved.get());
            hostFamilyProfile.setGenderPreferred(genderSaved.get().getGenderName());
            // Usar MapperProfile para obtener la lista de países preferidos
            List<Country> preferredCountries = mapperProfile.mapCountriesFromNames(familyProfileUpdateDTO.getCountries());

            // Guardar el perfil de la familia anfitriona
            HostFamilyProfile hostFamilyProfileSaved = hostFamilyProfileRepository.save(hostFamilyProfile);

            // Crear y guardar las relaciones intermedias
            for (Country country : preferredCountries) {
                HostFamilyPreferredCountry hostFamilyPreferredCountry = new HostFamilyPreferredCountry();
                hostFamilyPreferredCountry.setHostFamilyProfile(hostFamilyProfileSaved);
                hostFamilyPreferredCountry.setCountry(country);
                this.hostFamilyPreferredCountry.saveAndFlush(hostFamilyPreferredCountry);
                //revisar la manera en que se guarda porque se duplican los registros
            }
            this.hostFamilyProfileRepository.saveAndFlush(hostFamilyProfile);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Perfil familia actualizado"));
        } catch (Exception e) {
            log.error("Error en updateHostProfile" + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal"));

        }
    }
}