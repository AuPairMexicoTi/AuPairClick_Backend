package com.aupair.aupaircl.service.userservice.mapperuser;

import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import com.aupair.aupaircl.model.aupairpreferredcountry.AuPairPreferredCountry;

import java.util.List;


public class MapperUser {
    private MapperUser (){

    }
    public static List<CountryDTO> mapAuPairPreferredCountry(List<AuPairPreferredCountry> list){
        return list.stream().map(MapperUser::mapAuPairPreferredCountry).toList();
    }
    public static CountryDTO mapAuPairPreferredCountry(AuPairPreferredCountry auPairPreferredCountry){
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountryName(auPairPreferredCountry.getCountry().getCountryName());
        return countryDTO;
    }
}
