package com.aupair.aupaircl.controller.hostfamilyprofilecontroller;

import com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto.FamilyProfileUpdateDTO;
import com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto.FindHostFamilyDto;
import com.aupair.aupaircl.model.user.UserEmailDto;
import com.aupair.aupaircl.service.hostfamilyprofileservice.HostFamilyProfileService;
import com.aupair.aupaircl.utils.CustomResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hostFamilyProfile")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class HostFamilyProfileController {
    private final HostFamilyProfileService hostFamilyProfileService;

    @Autowired
    public HostFamilyProfileController(HostFamilyProfileService hostFamilyProfileService) {
        this.hostFamilyProfileService = hostFamilyProfileService;
    }

    @GetMapping(value = "/getHostProfile/{email}", produces = "application/json")
    public ResponseEntity<CustomResponse> getHostFamilyProfile(@PathVariable("email") String email) {
        try {
            return this.hostFamilyProfileService.getHostFamilyProfile(email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.BAD_REQUEST.value(), "Algo sucedio al obtener el perfil familiar"));
        }
    }

    @PutMapping(value = "/updateFamilyProfile", produces = "application/json")
    public ResponseEntity<CustomResponse> updateHostFamilyProfile(@RequestBody FamilyProfileUpdateDTO hostFamilyProfileDTO) {
        try {
            return this.hostFamilyProfileService.updateHostFamilyProfile(hostFamilyProfileDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.BAD_REQUEST.value(), "Algo sucedio al actualizar el perfil familiar"));
        }
    }
    @PostMapping(value = "/findHostFamily", produces = "application/json")
    public ResponseEntity<CustomResponse> findHostFamily(@RequestBody FindHostFamilyDto findHostFamilyDto) {
        try {
            return this.hostFamilyProfileService.findHostFamilies(findHostFamilyDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.BAD_REQUEST.value(), "Algo sucedio al buscar el perfil familiar"));
        }
    }
    @PostMapping(value = "/findHostFamilyDashboard", produces = "application/json")
    public ResponseEntity<CustomResponse> findHostFamilyDashboard(@Valid @RequestBody UserEmailDto userEmailDto) {
        try {
            return this.hostFamilyProfileService.findHostFamiliesDashboard(userEmailDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.BAD_REQUEST.value(), "Algo sucedio al encontrar el perfil familiar"));
        }
    }
    @GetMapping(value = "countHostFamilies",produces = "application/json")
    public ResponseEntity<CustomResponse> countHostFamilies() {
        try {
            return this.hostFamilyProfileService.countHostFamilies();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio al contar familias"));
        }
    }
    @GetMapping(value = "getHostProfileByNumPerfil/{numPerfil}", produces = "application/json")
    public ResponseEntity<CustomResponse> getHostProfileByNumPerfil(@PathVariable String numPerfil) {
        try {
            return this.hostFamilyProfileService.getHostProfileByNumPerfil(numPerfil);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio al obtener el perfil familiar"));
        }
    }
}