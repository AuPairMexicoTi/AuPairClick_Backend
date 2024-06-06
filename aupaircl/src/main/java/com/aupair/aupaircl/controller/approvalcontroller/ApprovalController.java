package com.aupair.aupaircl.controller.approvalcontroller;

import com.aupair.aupaircl.controller.approvalcontroller.approvaldto.ApprovalDTO;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.service.approvalservice.ApprovalService;
import com.aupair.aupaircl.service.mailservice.MailService;
import com.aupair.aupaircl.service.notificationservice.NotificationService;
import com.aupair.aupaircl.utils.CustomResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/approval")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class ApprovalController {
    private static final Logger log = LoggerFactory.getLogger(ApprovalController.class);
    private final ApprovalService approvalService;
    private final ProfileRepository profileRepository;
    private final AuPairProfileRepository auPairProfileRepository;
    private final HostFamilyProfileRepository hostFamilyProfileRepository;
    private final UserRepository userRepository;
    private final MailService mailService;
    public ApprovalController(ProfileRepository profileRepository,
                              ApprovalService approvalService
    ,AuPairProfileRepository auPairProfileRepository,HostFamilyProfileRepository hostFamilyProfileRepository,
                              UserRepository userRepository,MailService mailService) {
        this.approvalService = approvalService;
        this.profileRepository = profileRepository;
        this.auPairProfileRepository = auPairProfileRepository;
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    @PostMapping(value="/profile")
    public ResponseEntity<CustomResponse> approvedProfile(@RequestBody ApprovalDTO approvalDTO){
        Optional<Profile> userFind = profileRepository.findByUser_Email(approvalDTO.getEmail());
        if(userFind.isEmpty()){
         log.error("User not found to profile");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
        }
        if(Boolean.TRUE.equals(userFind.get().getIsApproved())){
            log.error("User already approved");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario ya ha sido aprobada"));
        }
        approvalService.approveProfileSection(userFind.get().getProfileId(),approvalDTO.getIsApproved());
        mailService.sendEmail(approvalDTO.getEmail(), "Aprovacion de seccion Pefil", approvalDTO.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Actualizacion de seccion Perfil"));
    }

    @PostMapping(value="/aupair")
    public ResponseEntity<CustomResponse> approvedAuPairProfile(@RequestBody ApprovalDTO approvalDTO){
        Optional<AuPairProfile> userFindAuPair = auPairProfileRepository.findByUser_Email(approvalDTO.getEmail());
        if(userFindAuPair.isEmpty()){
         log.error("User not found to au pair profile");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no valido"));
        }
        if(Boolean.TRUE.equals(userFindAuPair.get().getIsApproved()) && Boolean.TRUE.equals(approvalDTO.getIsApproved())){
            log.error("Au pair already approved");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Au Pair ya ha sido aprobada"));
        }
        approvalService.approveAuPairProfileSection(userFindAuPair.get().getAuPairProfileId(), approvalDTO.getIsApproved());
        mailService.sendEmail(approvalDTO.getEmail(), "Aprovacion de informacion de Au Pair", approvalDTO.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Actualizacion de seccion Au Pair"));
    }

    @PostMapping(value = "/hostfamily")
    public ResponseEntity<CustomResponse> approvedHostFamilyProfile(@RequestBody ApprovalDTO approvalDTO){
        Optional<HostFamilyProfile> userFindHost = hostFamilyProfileRepository.findByUser_Email(approvalDTO.getEmail());
        if(userFindHost.isEmpty()){
         log.error("User not found to host family profile");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no encontrado"));
        }
        if(Boolean.TRUE.equals(userFindHost.get().getIsApproved()) && Boolean.TRUE.equals(approvalDTO.getIsApproved())){
            log.error("Host family already approved");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Familia ya ha sido aprobada"));
        }
        approvalService.approveHostFamilyProfileSection(userFindHost.get().getHostFamilyProfileId(), approvalDTO.getIsApproved());
        mailService.sendEmail(approvalDTO.getEmail(), "Aprovacion de informacion de familia", approvalDTO.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Actualizacion de seccion Familia"));
    }
    @PostMapping(value = "/approvalAccount")
    public ResponseEntity<CustomResponse> approvedAccount(@RequestBody ApprovalDTO approvalDTO){
        try {
            Optional<User> user = this.userRepository.findByEmail(approvalDTO.getEmail());
            if (user.isEmpty()){
                log.error("Could not find");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no encontrado"));
            }
            if(isProfileAuPairCompletely(approvalDTO.getEmail())){
                if(Boolean.FALSE.equals(user.get().getIsLocked()) && Boolean.FALSE.equals(approvalDTO.getIsApproved())){
                    log.error("User already approved");
                    return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario ya ha sido aprobada"));
                }
                approvalService.approveAccount(user.get().getUserId(),approvalDTO.getIsApproved());
                mailService.sendEmail(approvalDTO.getEmail(), "Aprovacion de cuenta", approvalDTO.getMessage());
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Actualizacion de cuenta"));
            }else{
                log.error("Perfiles incompletos de aprovar");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Alguna seccion del perfil no ah sido aprovada"));
            }

        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }
    public boolean isProfileHostFamilyCompletely(String email) {
        Optional<Profile> profile = profileRepository.findByUser_Email(email);
        Optional<HostFamilyProfile> hostFamilyProfile = hostFamilyProfileRepository.findByUser_Email(email);

        boolean profileApproved = profile.isPresent() && profile.get().getIsApproved();
        boolean hostFamilyProfileApproved = hostFamilyProfile.isPresent() || hostFamilyProfile.get().getIsApproved();

        return profileApproved  && hostFamilyProfileApproved;
    }
    public boolean isProfileAuPairCompletely(String email){
        Optional<AuPairProfile> auPairProfile = auPairProfileRepository.findByUser_Email(email);
        Optional<Profile> profile = profileRepository.findByUser_Email(email);
        boolean profileApproved = profile.isPresent() && profile.get().getIsApproved();
        boolean profileAupairApproved = auPairProfile.get().getIsApproved();
        return profileApproved && profileAupairApproved;
    }
}