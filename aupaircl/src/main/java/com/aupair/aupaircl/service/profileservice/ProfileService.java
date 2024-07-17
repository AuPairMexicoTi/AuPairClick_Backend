package com.aupair.aupaircl.service.profileservice;

import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import com.aupair.aupaircl.controller.profilecontroller.profiledto.ProfileDTO;
import com.aupair.aupaircl.controller.profilecontroller.profiledto.ProfileUpdateDTO;
import com.aupair.aupaircl.controller.profilecontroller.profiledto.ResponseProfileAuPairDto;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountryRepository;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.contactdetails.ContactDetails;
import com.aupair.aupaircl.model.contactdetails.ContactDetailsRepository;
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
import com.aupair.aupaircl.service.userservice.mapperuser.MapperUser;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
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
    private final ContactDetailsRepository contactDetailsRepository;
    private static final String AuPairType= "aupair";
    private static final String FamilyType = "family";
    SecureRandom random = new SecureRandom();

    @Autowired
    public ProfileService(ProfileRepository profileRepository,ImageRepository imageRepository,CountryRepository countryRepository,
                          RolRepository rolRepository, LadaRepository ladaRepository,
                          HostFamilyProfileRepository hostFamilyProfileRepository,
                          AuPairProfileRepository auPairProfileRepository, UserRepository userRepository,
                          LocationTypesRepository locationTypeRepository,
                          GenderRepository genderRepository,
            MapperProfile mapperProfile,HostFamilyPreferredCountryRepository hostFamilyPreferredCountryRepository,
                          AuPairPreferredCountryRepository auPairPreferredCountryRepository,ContactDetailsRepository contactDetailsRepository) {
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
        this.contactDetailsRepository = contactDetailsRepository;
    }
    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> registerProfile(ProfileDTO profileDTO){
            try {
            Optional<User> userSaved = this.userRepository.findByEmail(profileDTO.getEmail());
            if (userSaved.isEmpty()){
                log.error("Could not find user in register");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario sin registrarse"));
            }

            if (Boolean.FALSE.equals(userSaved.get().isEmailVerified())) {
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
            Optional<LocationTypes> locationTypeSaved = this.locationTypesRepository.findByLocationTypeName(profileDTO.getLocationType());
            if (locationTypeSaved.isEmpty()) {
                log.error("Could not find location type");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Location invalida"));
            }
            Optional<Lada> ladaSaved = this.ladaRepository.findByLadaName(profileDTO.getLada());
            if (ladaSaved.isEmpty()){
                log.error("Could not find lada");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Lada invalida"));
            }
            Optional<Country> countrySaved = this.countryRepository.findByCountryName(profileDTO.getCountryOfResidence());
            if (countrySaved.isEmpty()) {
                log.error("Could not find country");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Pais invalido"));
            }
            Optional<Gender> genderSaved = this.genderRepository.findByGenderName(profileDTO.getGender());
            if (genderSaved.isEmpty() && profileDTO.getIsType().equals(AuPairType)){
                log.error("Could not find gender");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Genero invalido"));
            }

                Profile profile = new Profile();
                String numPerfil = genereteRandomCode();
                profile.setNumPerfil(numPerfil);
                profile.setFirstName(profileDTO.getFirstName());
                profile.setLastName(profileDTO.getLastName());
                profile.setSurname(profileDTO.getSurname());
                profile.setAge(profileDTO.getAge());
                profile.setCountry(countrySaved.get());
                profile.setRegion(profileDTO.getRegion());
                profile.setAboutMe(profileDTO.getAboutMe());
                profile.setLanguageOur(profileDTO.getLanguageOur());
                profile.setLanguageOurOther(profileDTO.getLanguageOurOther());
                profile.setLanguageOther(profileDTO.getLanguageOther());
                profile.setMinStayMonths(profileDTO.getMinStayMonths());
                profile.setMaxStayMonths(profileDTO.getMaxStayMonths());
                profile.setUser(userSaved.get());
                profile.setApproved(true);
                profile.setLocationType(locationTypeSaved.get());

            Profile profileSaved = this.profileRepository.save(profile);
                //Guarda las imagenes
                List<Image> imageList= Arrays.stream(profileDTO.getImages())
                        .map(image -> new Image(null,image.getImageName(),image.getImageLabel(),profileSaved))
                        .toList();
                this.imageRepository.saveAllAndFlush(imageList);
                if(profileDTO.getIsType().equals(AuPairType)){
                    AuPairProfile auPairProfile = new  AuPairProfile();
                    auPairProfile.setGender(genderSaved.get());
                    auPairProfile.setAvailableFrom(profileDTO.getAvailableFrom());
                    auPairProfile.setAvailableTo(profileDTO.getAvailableTo());
                    auPairProfile.setChildrenAgeMinSearch(profileDTO.getChildrenAgeMinFind());
                    auPairProfile.setChildrenAgeMaxSearch(profileDTO.getChildrenAgeMaxFind());
                    auPairProfile.setUser(userSaved.get());
                    auPairProfile.setSmoker(profileDTO.isSmoker());
                    auPairProfile.setDrivingLicence(profileDTO.isDrivingLicence());
                    auPairProfile.setVegetarian(profileDTO.isVegetarian());
                    auPairProfile.setChildCareExp(profileDTO.isChildCareExp());
                    auPairProfile.setFamilySmokes(profileDTO.isFamilySmokes());
                    auPairProfile.setHouseWork(profileDTO.isHouseWork());
                    auPairProfile.setWorkSpecialChildren(profileDTO.isWorkSpecialChildren());
                    auPairProfile.setToFamily(profileDTO.getToFamily());
                    auPairProfile.setApproved(true);
                    //Guardar perfil au pair

                   AuPairProfile auPairProfileSaved = this.auPairProfileRepository.save(auPairProfile);

                   //Detalles de contacto
                    ContactDetails contactDetails = new ContactDetails();
                    contactDetails.setStreet(profileDTO.getStreet());
                    contactDetails.setProvince(profileDTO.getProvince());
                    contactDetails.setZipCode(profileDTO.getZipCode());
                    contactDetails.setPhone(profileDTO.getPhone());
                    contactDetails.setCity(profileDTO.getCity());
                    contactDetails.setProfile(profileSaved);
                    this.contactDetailsRepository.save(contactDetails);
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
                    hostFamilyProfile.setHostingExperience(profileDTO.getHostingExperience());
                    hostFamilyProfile.setHouseDescription(profileDTO.getHouseDescription());
                    hostFamilyProfile.setChildrenAgesMin(profileDTO.getChildrenAgeMin());
                    hostFamilyProfile.setChildrenAgesMax(profileDTO.getChildrenAgeMax());
                    hostFamilyProfile.setNumberOfChildren(profileDTO.getNumberOfChildren() != null ? profileDTO.getNumberOfChildren() : 0); // Proveer valor por defecto
                    hostFamilyProfile.setSearchFrom(profileDTO.getSearchFrom());
                    hostFamilyProfile.setSearchTo(profileDTO.getSearchTo());
                    hostFamilyProfile.setLada(ladaSaved.get());
                    hostFamilyProfile.setSmokes(profileDTO.isSmokes());
                    hostFamilyProfile.setUser(userSaved.get());
                    hostFamilyProfile.setGenderPreferred(profileDTO.getGenderPreferred());
                    hostFamilyProfile.setApproved(true);
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
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Problema al registrar perfil"));
        }
        return null;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> updateProfile(ProfileUpdateDTO profileUpdateDTO) {
        try {

            Optional<User> userSaved = this.userRepository.findByEmail(profileUpdateDTO.getEmail());
            if (userSaved.isEmpty()){
                log.error("Could not find user in update");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido al actualizar"));
            }

            if (Boolean.FALSE.equals(userSaved.get().isEmailVerified())) {
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
    public ResponseEntity<CustomResponse> getProfileAuPairByEmail(String email) {
        try {
            Profile userSave = this.profileRepository.findByUser_EmailAndIsApproved(email,true);
            AuPairProfile auPairProfile = this.auPairProfileRepository.findByUser_EmailAndIsApproved(email,true);
            if (userSave == null){
                log.error("Could not find user in get profile");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido al traer perfil"));
            }
            if (!userSave.getUser().getRole().getRoleName().equals(AuPairType)){
                log.error("Solicitud incorrecta para el usuario");
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
            responseProfileDto.setLanguageOur(userSave.getLanguageOur());
            responseProfileDto.setLanguageOurOther(userSave.getLanguageOurOther());
            responseProfileDto.setLanguageOther(userSave.getLanguageOther());
            responseProfileDto.setStartDate(userSave.getUser().getAuPairProfile().getAvailableFrom());
            responseProfileDto.setEndDate(userSave.getUser().getAuPairProfile().getAvailableTo());
            responseProfileDto.setMaxStayMonths(userSave.getMaxStayMonths());
            responseProfileDto.setMinStayMonths(userSave.getMinStayMonths());
            responseProfileDto.setLocationType(userSave.getLocationType().getLocationTypeName());
            responseProfileDto.setChildrenAgeMax(userSave.getUser().getAuPairProfile().getChildrenAgeMaxSearch());
            responseProfileDto.setChildrenAgeMin(userSave.getUser().getAuPairProfile().getChildrenAgeMinSearch());
            List<AuPairPreferredCountry> auPairPreferredCountry = this.auPairPreferredCountryRepository.findByAuPairProfile_User_Email(email);
            List<CountryDTO> countryDTOS = MapperUser.mapAuPairPreferredCountry(auPairPreferredCountry);
            responseProfileDto.setPreferredCountries(countryDTOS);
            responseProfileDto.setToFamily(auPairProfile.getToFamily());
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


            List<Image> imageList = this.imageRepository.findByProfile_User_EmailAndProfile_IsApproved(email,true);
            responseProfileDto.setImages(imageList);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Perfil: ",HttpStatus.OK.value(), false, responseProfileDto));
        }catch (Exception e){
            log.error("Fallo al obtener perfil de au pair" +e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio al obtener perfil de au pair"));
        }
    }

    public String genereteRandomCode () {
        long randomCode = 10000000000L + random.nextLong() % 90000000000L;
        return String.valueOf(randomCode);
    }
}
