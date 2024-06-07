package com.aupair.aupaircl.controller.familypreferredcountriescontroller.familypreferredcountriesdto;

import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class FamilyPreferredCountriesDTO {
private String email;
private CountryDTO [] countryDTOS;
}
