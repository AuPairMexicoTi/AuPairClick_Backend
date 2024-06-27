package com.aupair.aupaircl.controller.familypreferredcountriescontroller;

import com.aupair.aupaircl.controller.familypreferredcountriescontroller.familypreferredcountriesdto.FamilyPreferredCountriesDTO;
import com.aupair.aupaircl.service.hostfamilypreferredcountryservice.HostFamilyPreferredCountryService;
import com.aupair.aupaircl.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/familyPreferredCountries")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class FamilyPreferredCountriesController {
    private final HostFamilyPreferredCountryService hostFamilyPreferredCountryService;
    public FamilyPreferredCountriesController(HostFamilyPreferredCountryService hostFamilyPreferredCountryService) {
        this.hostFamilyPreferredCountryService = hostFamilyPreferredCountryService;
    }
    @PostMapping(value = "/updatePreferredCountriesFamitly")
    public ResponseEntity<CustomResponse> updatePreferredCountriesFamily(@RequestBody FamilyPreferredCountriesDTO familyPreferredCountriesDTO){
        try {
            return this.hostFamilyPreferredCountryService.updatePreferredCountriesFamily(familyPreferredCountriesDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal"));
        }
    }
}
