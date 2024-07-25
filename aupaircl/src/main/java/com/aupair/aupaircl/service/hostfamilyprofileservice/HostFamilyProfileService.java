package com.aupair.aupaircl.service.hostfamilyprofileservice;

import com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto.FamilyProfileUpdateDTO;
import com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto.FindHostFamilyDto;
import com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto.ResponseFindHostFamilyDto;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.gender.Gender;
import com.aupair.aupaircl.model.gender.GenderRepository;

import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.lada.Lada;
import com.aupair.aupaircl.model.lada.LadaRepository;
import com.aupair.aupaircl.model.locationtype.LocationTypes;
import com.aupair.aupaircl.model.locationtype.LocationTypesRepository;
import com.aupair.aupaircl.model.user.UserEmailDto;
import com.aupair.aupaircl.service.hostfamilyprofileservice.mapperhostprofile.MapperHostProfile;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class HostFamilyProfileService {
    private final HostFamilyProfileRepository hostFamilyProfileRepository;
    private final LocationTypesRepository locationTypesRepository;
    private final AuPairProfileRepository auPairProfileRepository;
    private final LadaRepository ladaRepository;
    private final GenderRepository genderRepository;
    public HostFamilyProfileService(HostFamilyProfileRepository hostFamilyProfileRepository,
                                    LocationTypesRepository locationTypesRepository,
                                    LadaRepository ladaRepository,GenderRepository genderRepository,AuPairProfileRepository auPairProfileRepository) {
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
        this.locationTypesRepository = locationTypesRepository;
        this.ladaRepository = ladaRepository;
        this.genderRepository = genderRepository;
        this.auPairProfileRepository = auPairProfileRepository;
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
            Optional<HostFamilyProfile> userSaved = this.hostFamilyProfileRepository.findByUser_Email(familyProfileUpdateDTO.getEmail());
            if (userSaved.isEmpty()){
                log.error("Could not find user");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
            }
            if (Boolean.FALSE.equals(userSaved.get().getUser().isEmailVerified())) {
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
            HostFamilyProfile hostFamilyProfile = userSaved.get();
            hostFamilyProfile.setHouseDescription(familyProfileUpdateDTO.getHouse_description());
            hostFamilyProfile.setChildrenAgesMin(familyProfileUpdateDTO.getChildren_Age_min());
            hostFamilyProfile.setChildrenAgesMax(familyProfileUpdateDTO.getChildren_Age_max());
            hostFamilyProfile.setNumberOfChildren(familyProfileUpdateDTO.getNumber_of_children());
            hostFamilyProfile.setSearchFrom(familyProfileUpdateDTO.getSearch_from());
            hostFamilyProfile.setSearchTo(familyProfileUpdateDTO.getSearch_to());
            hostFamilyProfile.setUser(userSaved.get().getUser());
            hostFamilyProfile.setGenderPreferred(genderSaved.get().getGenderName());

            // Guardar el perfil de la familia anfitriona
            hostFamilyProfileRepository.save(hostFamilyProfile);

            this.hostFamilyProfileRepository.saveAndFlush(hostFamilyProfile);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Perfil familia actualizado"));
        } catch (Exception e) {
            log.error("Error en updateHostProfile" + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal"));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> findHostFamilies(FindHostFamilyDto familyDto) {
        try {
            List<HostFamilyProfile> hostFamilyProfiles = hostFamilyProfileRepository.findHostFamilies(
                    familyDto.getAuPairCountry(), familyDto.getGender(), familyDto.getPreferredCountryIds(),
                    familyDto.getStartDate(), familyDto.getEndDate(), familyDto.getMinDuration(), familyDto.getMaxDuration());
            if (hostFamilyProfiles.isEmpty()) {
                log.error("No host families found");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.BAD_REQUEST.value(), "No se encontraron coincidencias"));
            }

            List<ResponseFindHostFamilyDto> responseFindHostFamilyDtos = MapperHostProfile.mapHostToResponseProfile(hostFamilyProfiles);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Coincidencias de familias", HttpStatus.OK.value(), false, responseFindHostFamilyDtos));
        } catch (Exception e) {
            log.error("Error in findHostFamilies: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio en la busqueda"));
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> findHostFamiliesDashboard(UserEmailDto userEmailDto) {
        try {
            Optional<AuPairProfile> auPairPreferences = this.auPairProfileRepository.findByUser_Email(userEmailDto.getEmail());
            if (auPairPreferences.isEmpty()){
                log.error("No esta regitrado correctamente el usuario au pair");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
            }
            List<String> preferencesList = new ArrayList<>();
            for (AuPairPreferredCountry auPairProfile : auPairPreferences.get().getPreferredCountries()){
                preferencesList.add(auPairProfile.getCountry().getCountryName());
            }
            List<HostFamilyProfile> hostFamilyProfiles = hostFamilyProfileRepository.findHostFamiliesDashboard(
                    auPairPreferences.get().getUser().getProfile().getCountry().getCountryName(),
                    auPairPreferences.get().getGender().getGenderName(),
                    auPairPreferences.get().getUser().getProfile().getLocationType().getLocationTypeName(),
                    preferencesList,
                    auPairPreferences.get().getAvailableFrom(),auPairPreferences.get().getAvailableTo(),
                    auPairPreferences.get().getUser().getProfile().getMinStayMonths(),
                    auPairPreferences.get().getUser().getProfile( ).getMaxStayMonths(),
                    auPairPreferences.get().getChildrenAgeMaxSearch(),
                    auPairPreferences.get().getChildrenAgeMinSearch(),
                    auPairPreferences.get().isHouseWork(),
                    auPairPreferences.get().isSingleFamily(),auPairPreferences.get().isFamilySmokes(),
                    auPairPreferences.get().isWorkSpecialChildren());
            if (hostFamilyProfiles.isEmpty()) {
                log.error("No se encontraron familias que hagan mach para el dashboard");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.BAD_REQUEST.value(), "No se encontraron coincidencias de familias"));
            }

            List<ResponseFindHostFamilyDto> responseFindHostFamilyDtos = MapperHostProfile.mapHostToResponseProfile(hostFamilyProfiles);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Coincidencias de familias", HttpStatus.OK.value(), false, responseFindHostFamilyDtos));
        } catch (Exception e) {
            log.error("Error in findHostFamilies: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio en la busqueda"));
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> countHostFamilies(){
        try {
            int countHostFamiliesApproved = this.hostFamilyProfileRepository.countHostFamilyProfileByIsApproved(true);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Total de familias aprobadas" , HttpStatus.OK.value(), false, countHostFamiliesApproved));
        }catch (Exception e){
            log.error("Error al contar familias: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal al contar familias"));
        }
    }
}
