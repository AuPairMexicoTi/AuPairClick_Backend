package com.aupair.aupaircl.service.hostfamilyprofileservice;

import com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto.FamilyProfileUpdateDTO;
import com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto.FindHostFamilyDto;
import com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto.ResponseFindHostFamilyDto;
import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import com.aupair.aupaircl.controller.profilecontroller.profiledto.ResponseProfileFamilyDto;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.gender.Gender;
import com.aupair.aupaircl.model.gender.GenderRepository;

import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountry;
import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountryRepository;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.image.Image;
import com.aupair.aupaircl.model.image.ImageRepository;
import com.aupair.aupaircl.model.lada.Lada;
import com.aupair.aupaircl.model.lada.LadaRepository;
import com.aupair.aupaircl.model.locationtype.LocationTypes;
import com.aupair.aupaircl.model.locationtype.LocationTypesRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import com.aupair.aupaircl.model.user.UserEmailDto;
import com.aupair.aupaircl.service.hostfamilyprofileservice.mapperhostprofile.MapperHostProfile;
import com.aupair.aupaircl.service.userservice.mapperuser.MapperUser;
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
    private final ProfileRepository profileRepository;
    private final LadaRepository ladaRepository;
    private final GenderRepository genderRepository;
    private final ImageRepository imageRepository;
    private final HostFamilyPreferredCountryRepository hostFamilyPreferredCountryRepository;
    public HostFamilyProfileService(HostFamilyProfileRepository hostFamilyProfileRepository,
                                    LocationTypesRepository locationTypesRepository,
                                    LadaRepository ladaRepository,GenderRepository genderRepository,
                                    AuPairProfileRepository auPairProfileRepository, ImageRepository imageRepository,
                                    ProfileRepository profileRepository,HostFamilyPreferredCountryRepository hostFamilyPreferredCountryRepository) {
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
        this.locationTypesRepository = locationTypesRepository;
        this.ladaRepository = ladaRepository;
        this.genderRepository = genderRepository;
        this.auPairProfileRepository = auPairProfileRepository;
        this.imageRepository = imageRepository;
        this.profileRepository = profileRepository;
        this.hostFamilyPreferredCountryRepository = hostFamilyPreferredCountryRepository;
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
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getHostProfileByNumPerfil(String numPerfil) {
        try {
            HostFamilyProfile hostFamilyProfile = this.hostFamilyProfileRepository.findByUser_Profile_NumPerfil(numPerfil);
            if (hostFamilyProfile == null) {
                log.error("No tiene perfil el usuario familia");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "Usuario invalido al traer perfil"));
            }
            Profile userSave = this.profileRepository.findByUser_EmailAndIsApproved(hostFamilyProfile.getUser().getEmail(), true);
            if (userSave == null) {
                log.error("No se puede encontrar el perfil");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "Usuario invalido al traer perfil"));
            }
            if (!userSave.getUser().getRole().getRoleName().equals("family")) {
                log.error("El rol no corresponse a la solicitud");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "Solicitud incorrecta para el usuario"));
            }
            ResponseProfileFamilyDto responseProfileFamilyDto = new ResponseProfileFamilyDto();
            responseProfileFamilyDto.setName(userSave.getFirstName());
            responseProfileFamilyDto.setLastname(userSave.getLastName());
            responseProfileFamilyDto.setSurname(userSave.getSurname());
            responseProfileFamilyDto.setCountry(userSave.getCountry().getCountryName());
            responseProfileFamilyDto.setNationality(userSave.getCountry().getNationality());
            responseProfileFamilyDto.setGenderPreferred(hostFamilyProfile.getGenderPreferred());
            responseProfileFamilyDto.setMinStayMonths(userSave.getMinStayMonths());
            responseProfileFamilyDto.setMaxStayMonths(userSave.getMaxStayMonths());
            responseProfileFamilyDto.setSearchFrom(hostFamilyProfile.getSearchFrom());
            responseProfileFamilyDto.setSearchTo(hostFamilyProfile.getSearchTo());
            responseProfileFamilyDto.setNumOfChildren(hostFamilyProfile.getNumberOfChildren());
            responseProfileFamilyDto.setChildrenAgeMin(hostFamilyProfile.getChildrenAgesMin());
            responseProfileFamilyDto.setChildrenAgeMax(hostFamilyProfile.getChildrenAgesMax());
            responseProfileFamilyDto.setLocationType(userSave.getLocationType().getLocationTypeName());
            responseProfileFamilyDto.setLastLogin(userSave.getUser().getLastLogin());
            responseProfileFamilyDto.setNumPerfil(userSave.getNumPerfil());
            responseProfileFamilyDto.setAboutMe(userSave.getAboutMe());
            responseProfileFamilyDto.setAupairExp(hostFamilyProfile.isAupairExp());
            responseProfileFamilyDto.setAreSingleFamily(hostFamilyProfile.isAreSingleFamily());
            responseProfileFamilyDto.setAupairCareChildrenNeed(hostFamilyProfile.isAupairCareChildrenNeed());
            responseProfileFamilyDto.setSmokesInFamily(hostFamilyProfile.isSmokesInFamily());
            responseProfileFamilyDto.setHavePets(hostFamilyProfile.isHavePets());
            responseProfileFamilyDto.setAupairSmoker(hostFamilyProfile.isAupairSmoker());
            responseProfileFamilyDto.setAupairDrivingLicense(hostFamilyProfile.isAupairDrivingLicense());
            responseProfileFamilyDto.setAupairHouseWork(hostFamilyProfile.isAupairHouseWork());
            responseProfileFamilyDto.setAupairLanguageOther(hostFamilyProfile.getAupairLanguageOther());
            responseProfileFamilyDto.setAupairLanguageOurOther(hostFamilyProfile.getAupairLanguageOurOther());
            responseProfileFamilyDto.setAupairAgeMin(hostFamilyProfile.getAupairAgeMin());
            responseProfileFamilyDto.setAupairAgeMax(hostFamilyProfile.getAupairAgeMax());
            List<Image> imageList = this.imageRepository.findByProfile_User_EmailAndProfile_IsApproved(hostFamilyProfile.getUser().getEmail(), true);
            responseProfileFamilyDto.setImages(imageList);
            List<HostFamilyPreferredCountry> auPairPreferredCountry = this.hostFamilyPreferredCountryRepository.findByHostFamilyProfile_UserEmail(hostFamilyProfile.getUser().getEmail());
            List<CountryDTO> countryDTOS = MapperUser.mapHostPreferredCountry(auPairPreferredCountry);
            responseProfileFamilyDto.setPreferredCountries(countryDTOS);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Perfil familia: ", HttpStatus.OK.value(), false, responseProfileFamilyDto));

        } catch (Exception e) {
            log.error("Error al obtener el perfil de familia: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal al obtener el número"));
        }
    }
}
