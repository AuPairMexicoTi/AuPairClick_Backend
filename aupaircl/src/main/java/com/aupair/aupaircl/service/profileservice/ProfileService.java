package com.aupair.aupaircl.service.profileservice;

import com.aupair.aupaircl.controller.profilecontroller.profiledto.ProfileDTO;
import com.aupair.aupaircl.controller.profilecontroller.profiledto.ProfileUpdateDTO;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountryRepository;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.country.Country;
import com.aupair.aupaircl.model.country.CountryRepository;
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
import com.aupair.aupaircl.model.locationtype.LocationTypesRepository;
import com.aupair.aupaircl.model.locationtype.LocationTypes;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import com.aupair.aupaircl.model.rol.RolRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.service.profileservice.mapperprofile.MapperProfile;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final LocationTypesRepository locationTypesRepository;
    private final CountryRepository countryRepository;
    private final GenderRepository genderRepository;
    private final UserRepository userRepository;
    private final HostFamilyProfileRepository hostFamilyProfileRepository;
    private final LadaRepository ladaRepository;
    private final AuPairProfileRepository auPairProfileRepository;
    private final ImageRepository imageRepository;
    private final RolRepository rolRepository;
    private final MapperProfile mapperProfile;
    private final AuPairPreferredCountryRepository auPairPreferredCountryRepository;
    private final HostFamilyPreferredCountryRepository hostFamilyPreferredCountryRepository;
    private static final String AuPairType= "aupair";
    private static final String FamilyType = "family";
    @Autowired
    public ProfileService(ProfileRepository profileRepository,ImageRepository imageRepository,CountryRepository countryRepository,
                          RolRepository rolRepository, LadaRepository ladaRepository,
                          HostFamilyProfileRepository hostFamilyProfileRepository,
                          AuPairProfileRepository auPairProfileRepository, UserRepository userRepository,
                          LocationTypesRepository locationTypeRepository,
                          GenderRepository genderRepository,
            MapperProfile mapperProfile,HostFamilyPreferredCountryRepository hostFamilyPreferredCountryRepository,
                          AuPairPreferredCountryRepository auPairPreferredCountryRepository) {
        this.profileRepository = profileRepository;
        this.genderRepository = genderRepository;
        this.countryRepository = countryRepository;
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
        this.userRepository = userRepository;
        this.ladaRepository = ladaRepository;
        this.locationTypesRepository = locationTypeRepository;
        this.auPairProfileRepository = auPairProfileRepository;
        this.rolRepository = rolRepository;
        this.imageRepository = imageRepository;
        this.mapperProfile = mapperProfile;
        this.auPairPreferredCountryRepository = auPairPreferredCountryRepository;
        this.hostFamilyPreferredCountryRepository = hostFamilyPreferredCountryRepository;
    }
    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> registerProfile(ProfileDTO profileDTO){
        System.out.println(profileDTO);
        try {

            Optional<User> userSaved = this.userRepository.findByEmail(profileDTO.getEmail());
            if (userSaved.isEmpty()){
                log.error("Could not find user");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
            }

            if (Boolean.FALSE.equals(userSaved.get().getEmailVerified())) {
                log.error("Email isnt verified");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no verificado"));
            }
            if(this.profileRepository.existsByUser_Email(profileDTO.getEmail())){
                log.error("User already exists");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Ya has creado tu perfil"));
            }
            if(this.rolRepository.findByRoleName(profileDTO.getIsType()).isEmpty()){
                log.error("Rol invalid");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Rol invalido"));
            }
            Optional<LocationTypes> locationTypeSaved = this.locationTypesRepository.findByLocationTypeName(profileDTO.getLocation_type());
            if (locationTypeSaved.isEmpty()) {
                log.error("Could not find location type");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Location invalida"));
            }
            Optional<Lada> ladaSaved = this.ladaRepository.findByLadaName(profileDTO.getLada());
            if (ladaSaved.isEmpty()){
                log.error("Could not find lada");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Lada invalida"));
            }
            Optional<Country> countrySaved = this.countryRepository.findByCountryName(profileDTO.getCountry_of_residence());
            if (countrySaved.isEmpty()) {
                log.error("Could not find country");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Pais invalido"));
            }
            Optional<Gender> genderSaved = this.genderRepository.findByGenderName(profileDTO.getGender());
            if (genderSaved.isEmpty() && profileDTO.getIsType().equals("aupair")){
                log.error("Could not find gender");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Genero invalido"));
            }

                Profile profile = new Profile();
                profile.setFirstName(profileDTO.getFirst_name());
                profile.setLastName(profileDTO.getLast_name());
                profile.setAge(profileDTO.getAge());
                profile.setCountry(countrySaved.get());
                profile.setAboutMe(profileDTO.getAbout_me());
                profile.setLanguagesSpoken(profileDTO.getLanguages_spoken());
                profile.setMinStayMonths(profileDTO.getMin_stay_months());
                profile.setMaxStayMonths(profileDTO.getMax_stay_months());
                profile.setUser(userSaved.get());
                profile.setLocationType(locationTypeSaved.get());

            Profile profileSaved = this.profileRepository.save(profile);
                //Guarda las imagenes
                List<Image> imageList= Arrays.stream(profileDTO.getImages())
                        .map(image -> new Image(null,image.getImageName(),image.getImageLabel(),profileSaved))
                        .toList();
                this.imageRepository.saveAllAndFlush(imageList);
                if(profileDTO.getIsType().equals(AuPairType)){
                    AuPairProfile auPairProfile = new  AuPairProfile();
                    auPairProfile.setSmokes(profileDTO.getSmoke());
                    auPairProfile.setGender(genderSaved.get());
                    auPairProfile.setMotivation(profileDTO.getMotivation());
                    auPairProfile.setAvailableFrom(profileDTO.getAvailable_from());
                    auPairProfile.setAvailableTo(profileDTO.getAvailable_to());
                    auPairProfile.setChildcareExperience(profileDTO.getChild_care_experience());
                    auPairProfile.setChildrenAgeMinSearch(profileDTO.getChildren_Age_min());
                    auPairProfile.setChildrenAgeMaxSearch(profileDTO.getChildren_Age_max());
                    auPairProfile.setUser(userSaved.get());
                    //Guardar perfil au pair

                   AuPairProfile auPairProfileSaved = this.auPairProfileRepository.save(auPairProfile);
                    // Usar MapperProfile para obtener la lista de países preferidos
                    List<Country> preferredCountries = mapperProfile.mapCountriesFromNames(profileDTO.getCountriesPreferences());

                    // Crear y guardar las relaciones intermedias
                    for (Country country : preferredCountries) {
                        AuPairPreferredCountry auPairPreferredCountry = new AuPairPreferredCountry();
                        auPairPreferredCountry.setAuPairProfile(auPairProfileSaved);
                        auPairPreferredCountry.setCountry(country);
                        this.auPairPreferredCountryRepository.save(auPairPreferredCountry);
                    }

                    return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Perfil au pair registrado"));
                }
                if(profileDTO.getIsType().equals(FamilyType)){
                    HostFamilyProfile hostFamilyProfile = new HostFamilyProfile();
                    hostFamilyProfile.setHostingExperience(profileDTO.getHostin_experience());
                    hostFamilyProfile.setHouseDescription(profileDTO.getHouse_description());
                    hostFamilyProfile.setChildrenAgesMin(profileDTO.getChildren_Age_min());
                    hostFamilyProfile.setChildrenAgesMax(profileDTO.getChildren_Age_max());
                    hostFamilyProfile.setNumberOfChildren(profileDTO.getNumber_of_children() != null ? profileDTO.getNumber_of_children() : 0); // Proveer valor por defecto
                    hostFamilyProfile.setSearchFrom(profileDTO.getSearch_from());
                    hostFamilyProfile.setSearchTo(profileDTO.getSearch_to());
                    hostFamilyProfile.setLada(ladaSaved.get());
                    hostFamilyProfile.setSmokes(profileDTO.getSmokes());
                    hostFamilyProfile.setUser(userSaved.get());
                    hostFamilyProfile.setGenderPreferred(profileDTO.getGenderPreferred());
                    // Usar MapperProfile para obtener la lista de países preferidos
                    List<Country> preferredCountries = mapperProfile.mapCountriesFromNames(profileDTO.getCountriesPreferences());

                    // Guardar el perfil de la familia anfitriona
                    HostFamilyProfile hostFamilyProfileSaved = hostFamilyProfileRepository.save(hostFamilyProfile);

                    // Crear y guardar las relaciones intermedias
                    for (Country country : preferredCountries) {
                        HostFamilyPreferredCountry hostFamilyPreferredCountry = new HostFamilyPreferredCountry();
                        hostFamilyPreferredCountry.setHostFamilyProfile(hostFamilyProfileSaved);
                        hostFamilyPreferredCountry.setCountry(country);
                        this.hostFamilyPreferredCountryRepository.save(hostFamilyPreferredCountry);
                    }

                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Perfil familia registrado"));
            }
        }catch (Exception e){
            log.error("Failed to register profile" +e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Perfil registrado"));
        }
        return null;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> updateProfile(ProfileUpdateDTO profileUpdateDTO) {
        try {

            Optional<User> userSaved = this.userRepository.findByEmail(profileUpdateDTO.getEmail());
            if (userSaved.isEmpty()){
                log.error("Could not find user");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
            }

            if (Boolean.FALSE.equals(userSaved.get().getEmailVerified())) {
                log.error("Email isnt verified");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no verificado"));
            }
            Optional<LocationTypes> locationTypeSaved = this.locationTypesRepository.findByLocationTypeName(profileUpdateDTO.getLocation_type());
            if (locationTypeSaved.isEmpty()) {
                log.error("Could not find location type");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Location invalida"));
            }

            Optional<Country> countrySaved = this.countryRepository.findByCountryName(profileUpdateDTO.getCountry_of_residence());
            if (countrySaved.isEmpty()) {
                log.error("Could not find country");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Pais invalido"));
            }

            Optional<Profile> profile = this.profileRepository.findByUser_Email(profileUpdateDTO.getEmail());
                profile.get().setUser(userSaved.get());
                profile.get().setAge(profileUpdateDTO.getAge());
                profile.get().setLastName(profileUpdateDTO.getLast_name());
                profile.get().setFirstName(profileUpdateDTO.getFirst_name());
                profile.get().setMinStayMonths(profileUpdateDTO.getMin_stay_months());
                profile.get().setAboutMe(profileUpdateDTO.getAbout_me());
                profile.get().setMaxStayMonths(profileUpdateDTO.getMax_stay_months());
                profile.get().setLanguagesSpoken(profileUpdateDTO.getLanguages_spoken());
                profile.get().setCountry(countrySaved.get());
                profile.get().setLocationType(locationTypeSaved.get());
            this.profileRepository.saveAndFlush(profile.get());
           return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Perfil actualizado"));

        }catch (Exception e){
            log.error("Failed to update profile" +e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.OK.value(), "Perfil actualizado"));
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getProfileByEmail(String email) {
        try {
            Profile userSave = this.profileRepository.findByUser_EmailAndIsApproved(email,true);
            if (userSave == null){
                log.error("Could not find user");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Perfil: ",HttpStatus.OK.value(), false, userSave));
        }catch (Exception e){
            log.error("Failed to get profile" +e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.OK.value(), "Perfil actualizado"));
        }
    }


}
