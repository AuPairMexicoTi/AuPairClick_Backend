package com.aupair.aupaircl.controller.profilecontroller.profiledto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ProfileUpdateDTO {
    private String email;
    private String first_name;
    private Integer age;
    private String last_name;
    private String phone;
    private String languages_spoken;
    private String nationality;
    private String country_of_residence;
    private String about_me;
    private Integer min_stay_months;
    private Integer max_stay_months;
    private String location_type;

}
