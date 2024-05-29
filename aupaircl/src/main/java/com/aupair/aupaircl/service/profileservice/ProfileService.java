package com.aupair.aupaircl.service.profileservice;

import com.aupair.aupaircl.controller.profilecontroller.profileDTO.ProfileDTO;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.country.Country;
import com.aupair.aupaircl.model.country.CountryRepository;
import com.aupair.aupaircl.model.gender.Gender;
import com.aupair.aupaircl.model.gender.GenderRepository;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.lada.Lada;
import com.aupair.aupaircl.model.lada.LadaRepository;
import com.aupair.aupaircl.model.locationtype.LocationTypesRepository;
import com.aupair.aupaircl.model.locationtype.LocationTypes;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
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
    private static final String AuPairType= "aupair";

    @Autowired
    public ProfileService(ProfileRepository profileRepository, LadaRepository ladaRepository, HostFamilyProfileRepository hostFamilyProfileRepository, AuPairProfileRepository auPairProfileRepository, UserRepository userRepository, LocationTypesRepository locationTypeRepository, CountryRepository countryRepository, GenderRepository genderRepository) {
        this.profileRepository = profileRepository;
        this.genderRepository = genderRepository;
        this.countryRepository = countryRepository;
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
        this.userRepository = userRepository;
        this.ladaRepository = ladaRepository;
        this.locationTypesRepository = locationTypeRepository;
        this.auPairProfileRepository = auPairProfileRepository;
    }
    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> registerProfile(ProfileDTO profileDTO){
        try {
            Optional<User> userSaved = this.userRepository.findByEmail(profileDTO.getEmail());
            if (userSaved.isEmpty()){
                log.error("Could not find user");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "User not found"));
            }
            Optional<LocationTypes> locationTypeSaved = this.locationTypesRepository.findByLocationTypeName(profileDTO.getLocation_type());
            if (locationTypeSaved.isEmpty()) {
                log.error("Could not find location type");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Location type not found"));
            }
            Optional<Lada> ladaSaved = this.ladaRepository.findByLadaName(profileDTO.getLada());
            if (ladaSaved.isEmpty()){
                log.error("Could not find lada");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Lada not found"));
            }
            Optional<Country> countrySaved = this.countryRepository.findByCountryName(profileDTO.getCountry_of_residence());
            if (countrySaved.isEmpty()) {
                log.error("Could not find country");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Country not found"));
            }
            Optional<Gender> genderSaved = this.genderRepository.findByGenderName(profileDTO.getGender());
            if (genderSaved.isEmpty()){
                log.error("Could not find gender");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Gender not found"));
            }
            Profile profile = new Profile();
            profile.setAge(profileDTO.getAge());
            profile.setCountry(countrySaved.get());
            profile.setGender(genderSaved.get());
            profile.setAboutMe(profileDTO.getAbout_me());
            profile.setLanguagesSpoken(profile.getLanguagesSpoken());
            profile.setMinStayMonths(profileDTO.getMin_stay_months());
            profile.setMaxStayMonths(profileDTO.getMax_stay_months());
            if(profileDTO.getIsType().equals(AuPairType)){
                AuPairProfile auPairProfile = new  AuPairProfile();
                auPairProfile.setSmokes(profileDTO.getSmoke());
                auPairProfile.setMotivation(profileDTO.getMotivation());
                auPairProfile.setAvailableFrom(profileDTO.getAvailable_from());
                auPairProfile.setAvailableTo(profileDTO.getAvailable_to());
                auPairProfile.setChildcareExperience(profileDTO.getChild_care_experience());
                auPairProfile.setUser(userSaved.get());
                this.auPairProfileRepository.save(auPairProfile);

            }else{
                HostFamilyProfile hostFamilyProfile = new HostFamilyProfile();
                hostFamilyProfile.setHostingExperience(profileDTO.getHostin_experience());
                hostFamilyProfile.setHouseDescription(profileDTO.getHouse_description());
                hostFamilyProfile.setChildrenAges(profileDTO.getChildren_Age());
                hostFamilyProfile.setNumberOfChildren(profileDTO.getNumber_of_children());
                hostFamilyProfile.setSearchFrom(profileDTO.getSearch_from());
                hostFamilyProfile.setSearchTo(profileDTO.getSearch_to());
                hostFamilyProfile.setLada(ladaSaved.get());
                hostFamilyProfile.setSmokes(profileDTO.getSmokes());
                hostFamilyProfile.setUser(userSaved.get());
                hostFamilyProfile.setLocationType(locationTypeSaved.get());
                this.hostFamilyProfileRepository.save(hostFamilyProfile);
            }
            this.profileRepository.save(profile);
        }catch (Exception e){
            log.error("Failed to register profile" +e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Perfil registrado"));
        }
        return null;
    }
}
