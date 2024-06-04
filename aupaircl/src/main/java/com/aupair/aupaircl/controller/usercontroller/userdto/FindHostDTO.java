package com.aupair.aupaircl.controller.usercontroller.userdto;

import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FindHostDTO {
    private String email;
    private String countryFrom;
    private CountryDTO[] countriesTo;
    private String gender;
    private Date avaliableFrom;
    private Date avaliableTo;
    private Integer minMonths;
    private Integer maxMonths;
}
