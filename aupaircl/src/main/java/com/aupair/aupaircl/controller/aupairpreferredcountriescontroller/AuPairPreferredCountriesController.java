package com.aupair.aupaircl.controller.aupairpreferredcountriescontroller;

import com.aupair.aupaircl.controller.aupairpreferredcountriescontroller.aupairpreferredcountriesdto.AuPairPreferredCountriesDTO;
import com.aupair.aupaircl.service.aupairpreferredcountryservice.AuPairPreferredCountryService;
import com.aupair.aupaircl.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auPairPreferredCountries")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class AuPairPreferredCountriesController {
    private static AuPairPreferredCountryService auPairPreferredCountryService;
    @Autowired
    public AuPairPreferredCountriesController (AuPairPreferredCountryService auPairPreferredCountryService){
        this.auPairPreferredCountryService = auPairPreferredCountryService;
    }
    @PostMapping(value = "/updatePreferredCountriesAuPair")
    private ResponseEntity<CustomResponse> updatePreferredCountriesAuPair(@RequestBody AuPairPreferredCountriesDTO auPairPreferredCountriesDTO){
        try {
            return auPairPreferredCountryService.aupairPreferredCountry(auPairPreferredCountriesDTO);
        }catch (Exception e){
            return ResponseEntity.status(500).body(new CustomResponse(true,500,"Algo salio mal"));
        }
    }
}
