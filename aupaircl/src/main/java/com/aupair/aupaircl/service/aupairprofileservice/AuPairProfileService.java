package com.aupair.aupaircl.service.aupairprofileservice;

import com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto.ProfileAuPairDTO;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountryRepository;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.gender.Gender;
import com.aupair.aupaircl.model.gender.GenderRepository;
import com.aupair.aupaircl.service.profileservice.mapperprofile.MapperProfile;
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
public class AuPairProfileService {
private final AuPairProfileRepository auPairProfileRepository;
    private final GenderRepository genderRepository;
    private final AuPairPreferredCountryRepository auPairPreferredCountryRepository;
    private final MapperProfile mapperProfile;

    @Autowired
public AuPairProfileService(AuPairProfileRepository auPairProfileRepository,
                            GenderRepository genderRepository, AuPairPreferredCountryRepository auPairPreferredCountryRepository, MapperProfile mapperProfile) {
    this.auPairProfileRepository = auPairProfileRepository;
    this.genderRepository = genderRepository;
    this.auPairPreferredCountryRepository = auPairPreferredCountryRepository;
        this.mapperProfile = mapperProfile;
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
            if (Boolean.FALSE.equals(userSaved.get().getUser().getEmailVerified())) {
                log.error("Email isnt verified");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no verificado"));
            }
            Optional<AuPairProfile> auPairProfile = this.auPairProfileRepository.findByUser_Email(profileAuPairDTO.getEmail());
            if (auPairProfile.isPresent()) {
                auPairProfile.get().setSmokes(profileAuPairDTO.getSmoke());
                auPairProfile.get().setMotivation(profileAuPairDTO.getMotivation());
                auPairProfile.get().setChildcareExperience(profileAuPairDTO.getChild_care_experience());
                auPairProfile.get().setUser(userSaved.get().getUser());
                auPairProfile.get().setAvailableFrom(profileAuPairDTO.getAvailable_from());
                auPairProfile.get().setAvailableTo(profileAuPairDTO.getAvailable_to());
                auPairProfile.get().setGender(genderSaved.get());
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

}
