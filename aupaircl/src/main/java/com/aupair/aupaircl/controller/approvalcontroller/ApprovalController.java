package com.aupair.aupaircl.controller.approvalcontroller;

import com.aupair.aupaircl.controller.approvalcontroller.approvaldto.ApprovalDTO;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import com.aupair.aupaircl.service.approvalservice.ApprovalService;
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
    private final NotificationService notificationService;
    private final AuPairProfileRepository auPairProfileRepository;
    private final HostFamilyProfileRepository hostFamilyProfileRepository;

    public ApprovalController(ProfileRepository profileRepository,
                              ApprovalService approvalService,NotificationService notificationService
    ,AuPairProfileRepository auPairProfileRepository,HostFamilyProfileRepository hostFamilyProfileRepository) {
        this.approvalService = approvalService;
        this.notificationService = notificationService;
        this.profileRepository = profileRepository;
        this.auPairProfileRepository = auPairProfileRepository;
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
    }


    @PostMapping(value="/profile")
    public ResponseEntity<CustomResponse> approvedProfile(@RequestBody ApprovalDTO approvalDTO){
        Optional<Profile> userFind = profileRepository.findByUser_Email(approvalDTO.getEmail());
        if(userFind.isEmpty()){
         log.error("User not found to profile");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
        }

        approvalService.approveProfileSection(userFind.get().getProfileId(),approvalDTO.isApproved());
        notificationService.sendApprovalNotification(approvalDTO.getEmail(), "Perfil", approvalDTO.isApproved());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Actualizacion de seccion Perfil"));
    }


    @PostMapping(value="/aupair")
    public ResponseEntity<CustomResponse> approvedAuPairProfile(@RequestBody ApprovalDTO approvalDTO){
        Optional<AuPairProfile> userFindAuPair = auPairProfileRepository.findByUser_Email(approvalDTO.getEmail());
        if(userFindAuPair.isEmpty()){
         log.error("User not found to au pair profile");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no valido"));
        }
        if (Boolean.TRUE.equals(userFindAuPair.get().getIsApproved())) {
            log.error("Au pair profile already approved");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Au Pair perfil ya ha sido aprobado"));
        }
        approvalService.approveAuPairProfileSection(userFindAuPair.get().getAuPairProfileId(),false);
        notificationService.sendApprovalNotification(approvalDTO.getEmail(),"Perfil de au pair", approvalDTO.isApproved());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Actualizacion de seccion Au Pair"));
    }


    @PostMapping(value = "/hostfamily")
    public ResponseEntity<CustomResponse> approvedHostFamilyProfile(@RequestBody ApprovalDTO approvalDTO){
        Optional<HostFamilyProfile> userFindHost = hostFamilyProfileRepository.findByUser_Email(approvalDTO.getEmail());
        if(userFindHost.isEmpty()){
         log.error("User not found to host family profile");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no encontrado"));
        }
        if(Boolean.TRUE.equals(userFindHost.get().getIsApproved())){
            log.error("Host family already approved");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Familia ya ha sido aprobada"));
        }
        approvalService.approveHostFamilyProfileSection(userFindHost.get().getHostFamilyProfileId(),false);
        notificationService.sendApprovalNotification(approvalDTO.getEmail(),"Perfil de familia anfitriona", approvalDTO.isApproved());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Actualizacion de seccion Familia"));
    }
}