package com.aupair.aupaircl.service.aupairprofileservice;

import com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto.FindAuPairDTO;
import com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto.ProfileAuPairDTO;
import com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto.ResponseFindAuPair;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.gender.Gender;
import com.aupair.aupaircl.model.gender.GenderRepository;
import com.aupair.aupaircl.service.aupairprofileservice.mapperaupairprofile.MapperAuPairProfile;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class AuPairProfileService {
private final AuPairProfileRepository auPairProfileRepository;
    private final GenderRepository genderRepository;


    @Autowired
public AuPairProfileService(AuPairProfileRepository auPairProfileRepository,
                            GenderRepository genderRepository) {
    this.auPairProfileRepository = auPairProfileRepository;
    this.genderRepository = genderRepository;

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
}
