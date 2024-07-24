package com.aupair.aupaircl.controller.profileaupaircontroller;

import com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto.FindAuPairDTO;
import com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto.ProfileAuPairDTO;
import com.aupair.aupaircl.model.user.UserEmailDto;
import com.aupair.aupaircl.service.aupairprofileservice.AuPairProfileService;
import com.aupair.aupaircl.utils.CustomResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profileAuPair")
@CrossOrigin(origins = "http://localhost:5173/")
@Slf4j
public class ProfileAuPairController {
    private final AuPairProfileService profileAuPairService;

    public ProfileAuPairController(AuPairProfileService profileAuPairService) {
        this.profileAuPairService = profileAuPairService;
    }
    @GetMapping(value = "/getPerfilAuPair/{email}", produces = "application/json")
    public ResponseEntity<CustomResponse> getAuPairProfile(@PathVariable String email) {
        try {
            return this.profileAuPairService.getAuPairProfile(email);
        }catch (Exception e){
            log.error("Error"+e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal al obtener perfil"));
        }
    }
    @PutMapping(value = "/updateProfileAuPair", produces = "application/json")
    public ResponseEntity<CustomResponse> updateProfileAuPair(@RequestBody ProfileAuPairDTO profileDTO) {
        try {
            return this.profileAuPairService.updateProfileAuPair(profileDTO);
        }catch (Exception e){
            log.error("Algo sucedio al actualizar seccion Au Pair");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal al actualizar"));
        }
    }
    @PostMapping(value = "/findAuPair", produces = "application/json")
    public ResponseEntity<CustomResponse> findAuPair(@RequestBody FindAuPairDTO findAuPairDTO) {
        try {
            return this.profileAuPairService.findAuPair(findAuPairDTO);
        }catch (Exception e){
            log.error("Algo sucedio al buscar Au Pair");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal al buscar Au Pair"));
        }
    }
    @PostMapping(value = "/findAuPairDashboard", produces = "application/json")
    public ResponseEntity<CustomResponse> findAuPairDasboard(@Valid @RequestBody UserEmailDto userEmailDto) {
        try {
            return this.profileAuPairService.findAuPairDashboard(userEmailDto);
        }catch (Exception e){
            log.error("Algo sucedio al buscar Au Pair");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal al buscar Au Pair"));
        }
    }
    @GetMapping(value = "/getCountAuPairs",produces = "application/json")
    public ResponseEntity<CustomResponse> getCountAuPairs() {
        try {
            return this.profileAuPairService.countAuPairs();
        }catch (Exception e){
            log.error("Algo sucedio al contar Au Pair");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal al contar Au Pair"));
        }
    }
}

