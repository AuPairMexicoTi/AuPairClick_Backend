package com.aupair.aupaircl.controller.profilecontroller;

import com.aupair.aupaircl.controller.profilecontroller.profiledto.ProfileDTO;
import com.aupair.aupaircl.controller.profilecontroller.profiledto.ProfileUpdateDTO;
import com.aupair.aupaircl.service.profileservice.ProfileService;
import com.aupair.aupaircl.utils.CustomResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = {"http://localhost:5173/"})
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(value = "/registerProfile", produces = "application/json")
    public ResponseEntity<CustomResponse> registerProfile(@Valid @RequestBody ProfileDTO profileDTO) {
        try {
            return this.profileService.registerProfile(profileDTO);
        } catch (Exception e) {
            log.error("Error en registro de perfil + e.getMessage()");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal en el registro de perfil"));
        }
    }
    @PutMapping(value = "/updateProfile", produces = "application/json")
    public ResponseEntity<CustomResponse> updateProfile(@RequestBody ProfileUpdateDTO profileDTO) {
        try {
            return this.profileService.updateProfile(profileDTO);
        } catch (Exception e) {
            log.error("Error en actualizacion de perfil + e.getMessage()");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal"));
        }
    }
    @GetMapping(value = "/getProfileAuPairByEmail/{email}",produces = "application/json")
    public ResponseEntity<CustomResponse> getProfileAuPairByEmail(@PathVariable("email") String email) {
        try {
            return this.profileService.getProfileAuPairByEmail(email);
        }catch (Exception e){
            log.error("Error"+e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal"));
        }
    }
}