package com.aupair.aupaircl.service.profileservice.mapperprofile;


import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import com.aupair.aupaircl.model.country.Country;
import com.aupair.aupaircl.model.country.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MapperProfile {
private static CountryRepository countryRepository;
public MapperProfile(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
}

    public List<Country> mapCountriesFromNames(CountryDTO[] countryDTOs) {
        return Arrays.stream(countryDTOs)
                .map(CountryDTO::getCountryName)
                .map(countryRepository::findByCountryName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
