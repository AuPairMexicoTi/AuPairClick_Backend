package com.aupair.aupaircl.service.aupairprofileservice;

import com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto.FindAuPairDTO;
import com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto.ProfileAuPairDTO;
import com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto.ResponseFindAuPair;
import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import com.aupair.aupaircl.controller.profilecontroller.profiledto.ResponseProfileAuPairDto;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountryRepository;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.gender.Gender;
import com.aupair.aupaircl.model.gender.GenderRepository;
import com.aupair.aupaircl.model.hostfamilypreferredcountry.HostFamilyPreferredCountry;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.image.Image;
import com.aupair.aupaircl.model.image.ImageRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import com.aupair.aupaircl.model.user.UserEmailDto;
import com.aupair.aupaircl.service.aupairprofileservice.mapperaupairprofile.MapperAuPairProfile;
import com.aupair.aupaircl.service.userservice.mapperuser.MapperUser;
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
public class AuPairProfileService {
private final AuPairProfileRepository auPairProfileRepository;
    private final GenderRepository genderRepository;
    private final HostFamilyProfileRepository hostFamilyProfileRepository;
    private final MapperAuPairProfile mapperAuPairProfile;
    private final AuPairPreferredCountryRepository auPairPreferredCountryRepository;
    private final ImageRepository imageRepository;
    private final ProfileRepository profileRepository;
    @Autowired
public AuPairProfileService(AuPairProfileRepository auPairProfileRepository,
                            GenderRepository genderRepository,HostFamilyProfileRepository hostFamilyProfileRepository,
    MapperAuPairProfile mapperAuPairProfile, AuPairPreferredCountryRepository auPairPreferredCountryRepository,
                            ImageRepository imageRepository,ProfileRepository profileRepository) {
    this.auPairProfileRepository = auPairProfileRepository;
    this.genderRepository = genderRepository;
    this.hostFamilyProfileRepository = hostFamilyProfileRepository;
    this.mapperAuPairProfile = mapperAuPairProfile;
    this.auPairPreferredCountryRepository = auPairPreferredCountryRepository;
    this.imageRepository = imageRepository;
    this.profileRepository = profileRepository;
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getAuPairProfile(String email){
        AuPairProfile auPairProfile = auPairProfileRepository.findByUser_EmailAndIsApproved(email,true);
        if (auPairProfile != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Perfil Au Pair: ",HttpStatus.OK.value(), false,auPairProfile));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.BAD_REQUEST.value(), "Perfil no encontrado"));
        }
    }
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> updateProfileAuPair(ProfileAuPairDTO profileAuPairDTO) {
        try {
            Optional<AuPairProfile> userSaved = this.auPairProfileRepository.findByUser_Email(profileAuPairDTO.getEmail());
            if (userSaved.isEmpty()){
                log.error("Could not find user");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
            }
            Optional<Gender> genderSaved = this.genderRepository.findByGenderName(profileAuPairDTO.getGender());
            if (genderSaved.isEmpty()){
                log.error("Could not find gender");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Genero invalido"));
            }
            if (Boolean.FALSE.equals(userSaved.get().getUser().isEmailVerified())) {
                log.error("Email isnt verified");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no verificado"));
            }
            Optional<AuPairProfile> auPairProfile = this.auPairProfileRepository.findByUser_Email(profileAuPairDTO.getEmail());
            if (auPairProfile.isPresent()) {
                auPairProfile.get().setSmoker(profileAuPairDTO.getSmoke());
                auPairProfile.get().setUser(userSaved.get().getUser());
                auPairProfile.get().setAvailableFrom(profileAuPairDTO.getAvailable_from());
                auPairProfile.get().setAvailableTo(profileAuPairDTO.getAvailable_to());
                auPairProfile.get().setGender(genderSaved.get());
                auPairProfile.get().setChildrenAgeMinSearch(profileAuPairDTO.getChildrenAgeMinFind());
                auPairProfile.get().setChildrenAgeMaxSearch(profileAuPairDTO.getChildrenAgeMaxFind());
             this.auPairProfileRepository.save(auPairProfile.get());
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Perfil actualizado"));
            }else{
                log.error("User with Au Pair profile not found");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
            }

        }catch (Exception e){
            log.error("Error updating profile au pair");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.OK.value(), "Perfil actualizado"));
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> findAuPair (FindAuPairDTO findAuPairDTO){
        try {
        List<AuPairProfile> auPairProfiles = this.auPairProfileRepository.findAuPair(findAuPairDTO.getFamilyCountry(),findAuPairDTO.getGenderSearch(),findAuPairDTO.getPreferredCountryNames(),
                findAuPairDTO.getStartDate(),findAuPairDTO.getEndDate(),findAuPairDTO.getMinDuration(), findAuPairDTO.getMaxDuration());
            if (auPairProfiles.isEmpty()) {
                log.error("No hay registros que hagan mach");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.BAD_REQUEST.value(), "No hay coincidencias"));
            }
            List<ResponseFindAuPair> responseFindAuPairs = MapperAuPairProfile.mapAuPairToResponseProfile(auPairProfiles);

            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Coincidencia de Au Pairs", HttpStatus.OK.value(), false, responseFindAuPairs));
        }catch (Exception e) {
        log.error("Algo sucedio en la busqueda avanzada de au pairs");
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio en la busqueda avanzada de au pairs"));
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> findAuPairDashboard (UserEmailDto userEmailDto){
        try {
            Optional<HostFamilyProfile> hostFamilyProfilePreferences = this.hostFamilyProfileRepository.findByUser_Email(userEmailDto.getEmail());
            if (hostFamilyProfilePreferences.isEmpty()){
                log.error("No esta registrada correctamente la familia");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "No esta registrado el perfil"));
            }
            List<String> preferencesCountries = new ArrayList<>();
            for (HostFamilyPreferredCountry hostFamilyPreferredCountry : hostFamilyProfilePreferences.get().getPreferredCountries()){
                preferencesCountries.add(hostFamilyPreferredCountry.getCountry().getCountryName());
            }
            List<AuPairProfile> auPairProfiles = this.auPairProfileRepository.findAuPairDashboard(
                    hostFamilyProfilePreferences.get().getUser().getProfile().getCountry().getCountryName(),
                    hostFamilyProfilePreferences.get().getGenderPreferred(),
                    preferencesCountries,
                    hostFamilyProfilePreferences.get().getSearchFrom(),
                    hostFamilyProfilePreferences.get().getSearchTo(),
                    hostFamilyProfilePreferences.get().getUser().getProfile().getMinStayMonths(),
                    hostFamilyProfilePreferences.get().getUser().getProfile().getMaxStayMonths(),
                    hostFamilyProfilePreferences.get().getAupairLanguageOurOther(),
                    hostFamilyProfilePreferences.get().getAupairLanguageOther(),
                    hostFamilyProfilePreferences.get().isAupairSmoker(),
                    hostFamilyProfilePreferences.get().isAupairDrivingLicense(),
                    hostFamilyProfilePreferences.get().isAupairHouseWork());

            if (auPairProfiles.isEmpty()) {
                log.error("No hay registros que hagan mach");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.BAD_REQUEST.value(), "No hay coincidencias de au pairs"));
            }
            List<ResponseFindAuPair> responseFindAuPairs = MapperAuPairProfile.mapAuPairToResponseProfile(auPairProfiles);

            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Coincidencia de Au Pairs", HttpStatus.OK.value(), false, responseFindAuPairs));
        }catch (Exception e) {
            log.error("Algo sucedio en la busqueda avanzada de au pairs");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio en la busqueda avanzada de au pairs"));
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> countAuPairs (){
        try {
            int numAuPaisApproved = this.auPairProfileRepository.countAuPairProfileByIsApproved(true);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Total de au pairs aprovados",HttpStatus.OK.value(),false,numAuPaisApproved));
        }catch (Exception e){
            log.error("Algo sucedio en el conteo de au pairs");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio en el conteo de au pairs"));
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getProfileByNumProfile(String numProfile){
        try {
            AuPairProfile auPairProfile = this.auPairProfileRepository.findByUser_Profile_NumPerfil(numProfile);
            if (auPairProfile == null){
                log.error("No se encontro el au pair por numero de perfil");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.NOT_FOUND.value(), "Au Pair no encontrado"));
            }
            Profile userSave = this.profileRepository.findByUser_EmailAndIsApproved(auPairProfile.getUser().getEmail(),true);
            if (userSave == null){
                log.error("Could not find user in get profile");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido al traer perfil"));
            }
            if (!userSave.getUser().getRole().getRoleName().equals("aupair")){
                log.error("Solicitud de rol diferente al obtener perfil");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Solicitud incorrecta para el usuario"));
            }
            ResponseProfileAuPairDto responseProfileDto = new ResponseProfileAuPairDto();
            responseProfileDto.setName(userSave.getFirstName());
            responseProfileDto.setLastname(userSave.getLastName());
            responseProfileDto.setGender(userSave.getUser().getAuPairProfile().getGender().getGenderName());
            responseProfileDto.setAge(userSave.getAge());
            responseProfileDto.setCountry(userSave.getCountry().getCountryName());
            responseProfileDto.setRegion(userSave.getRegion());
            responseProfileDto.setNationality(userSave.getCountry().getNationality());
            responseProfileDto.setLanguageOur(auPairProfile.getLanguageOur());
            responseProfileDto.setLanguageOurOther(auPairProfile.getLanguageOurOther());
            responseProfileDto.setLanguageOther(auPairProfile.getLanguageOther());
            responseProfileDto.setStartDate(userSave.getUser().getAuPairProfile().getAvailableFrom());
            responseProfileDto.setEndDate(userSave.getUser().getAuPairProfile().getAvailableTo());
            responseProfileDto.setMaxStayMonths(userSave.getMaxStayMonths());
            responseProfileDto.setMinStayMonths(userSave.getMinStayMonths());
            responseProfileDto.setLocationType(userSave.getLocationType().getLocationTypeName());
            responseProfileDto.setChildrenAgeMax(userSave.getUser().getAuPairProfile().getChildrenAgeMaxSearch());
            responseProfileDto.setChildrenAgeMin(userSave.getUser().getAuPairProfile().getChildrenAgeMinSearch());
            List<AuPairPreferredCountry> auPairPreferredCountry = this.auPairPreferredCountryRepository.findByAuPairProfile_User_Email(auPairProfile.getUser().getEmail());
            List<CountryDTO> countryDTOS = MapperUser.mapAuPairPreferredCountry(auPairPreferredCountry);
            responseProfileDto.setPreferredCountries(countryDTOS);
            responseProfileDto.setToFamily(userSave.getAboutMe());
            responseProfileDto.setLastLogin(userSave.getUser().getLastLogin());
            responseProfileDto.setNumPerfil(userSave.getNumPerfil());

            responseProfileDto.setChildCareExperience(auPairProfile.isChildCareExp());
            responseProfileDto.setVegetarian(auPairProfile.isVegetarian());
            responseProfileDto.setSingleFamily(auPairProfile.isSingleFamily());
            responseProfileDto.setSmoker(auPairProfile.isSmoker());
            responseProfileDto.setHouseWork(auPairProfile.isHouseWork());
            responseProfileDto.setDrivingLicence(auPairProfile.isDrivingLicence());
            responseProfileDto.setFamilySmokes(auPairProfile.isFamilySmokes());
            responseProfileDto.setWorkSpecialChildren(auPairProfile.isWorkSpecialChildren());


            List<Image> imageList = this.imageRepository.findByProfile_User_EmailAndProfile_IsApproved(auPairProfile.getUser().getEmail(),true);
            responseProfileDto.setImages(imageList);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Usuario encontrado", HttpStatus.OK.value(), false, responseProfileDto));
        }catch (Exception e){
            log.error("Algo sucedio al buscar el usuario por numero de cuenta");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio al buscar el usuario por numero de cuenta"));
        }
    }
}
