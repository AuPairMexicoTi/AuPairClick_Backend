package com.aupair.aupaircl.controller.aupairpreferredcountriescontroller.aupairpreferredcountriesdto;

import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AuPairPreferredCountriesDTO {
    private String email;
    private CountryDTO[] countryDTOS;
}
