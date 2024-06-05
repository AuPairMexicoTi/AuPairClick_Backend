package com.aupair.aupaircl.controller.profilecontroller.profiledto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
public class ProfileDTO {
    private String email;
    private String first_name;
    private Integer age;
    private String last_name;
    private String phone;
    private String lada;
    private String languages_spoken;
    private String nationality;
    private String country_of_residence;
    private String gender;
    private String about_me;
    private Integer min_stay_months;
    private Integer max_stay_months;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date available_from;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date available_to;
    private String child_care_experience;
    private String motivation;
    private Boolean smoke;


    private String children_Age;
    private String hostin_experience;
    private String house_description;
    private Integer number_of_children;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date search_from;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date search_to;
    private String location_type;
    private Boolean smokes;
    private String genderPreferred;

    private String isType;

    private ImageDTO[] images;
    private CountryDTO[] countriesPreferences;

}
