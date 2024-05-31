package com.aupair.aupaircl.controller.approvalcontroller;

import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
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


    @PostMapping(value="/profile/{email}")
    public ResponseEntity<CustomResponse> approvedProfile(@PathVariable("email") String email){
        Optional<Profile> userFind = profileRepository.findByUser_Email(email);
        if(userFind.isEmpty()){
         log.error("User not found to profile");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no encontrado"));
        }
        approvalService.approveProfileSection(userFind.get().getProfileId());
        notificationService.sendApprovalNotification(email,"Perfil",true);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Perfil aprobado"));
    }


    @PostMapping(value="/aupair/{email}")
    public ResponseEntity<CustomResponse> approvedAuPairProfile(@PathVariable("email") String email){
        Optional<Profile> userFindAuPair = profileRepository.findByUser_Email(email);
        if(userFindAuPair.isEmpty()){
         log.error("User not found to au pair profile");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no encontrado"));
        }
        approvalService.approveAuPairProfileSection(userFindAuPair.get().getProfileId());
        notificationService.sendApprovalNotification(email,"Perfil de au pair",true);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Perfil de au pair aprobado"));
    }


    @PostMapping(value = "/hostfamily/{email}")
    public ResponseEntity<CustomResponse> approvedHostFamilyProfile(@PathVariable("email") String email){
        Optional<Profile> userFindHost = profileRepository.findByUser_Email(email);
        if(userFindHost.isEmpty()){
         log.error("User not found to host family profile");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario no encontrado"));
        }
        approvalService.approveHostFamilyProfileSection(userFindHost.get().getProfileId());
        notificationService.sendApprovalNotification(email,"Perfil de familia anfitriona",true);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Perfil de familia anfitriona aprobado"));
    }
}