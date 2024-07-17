package com.aupair.aupaircl.controller.profilecontroller.profiledto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
public class ProfileDTO {
    private String email;
    private String firstName;
    private Integer age;
    private String lastName;
    private String surname;
    private String lada;
    private String languageOur;
    private String languageOurOther;
    private String languageOther;
    private String nationality;
    private String countryOfResidence;
    private String region;
    private String gender;
    private String aboutMe;
    private Integer minStayMonths;
    private Integer maxStayMonths;

    @DateTimeFormat(pattern = "yyyy-MM")
    private Date availableFrom;
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date availableTo;
    private Integer childrenAgeMinFind;
    private Integer childrenAgeMaxFind;
    private boolean childCareExp;
    private boolean smoker;
    private boolean familySmokes;
    private boolean drivingLicence;
    private boolean houseWork;
    private boolean workSpecialChildren;
    private boolean singleFamily;
    private boolean vegetarian;
    private String toFamily;

    private Integer childrenAgeMin;
    private Integer childrenAgeMax;
    private String hostingExperience;
    private String houseDescription;
    private Integer numberOfChildren;
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date searchFrom;
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date searchTo;
    private String locationType;
    private boolean smokes;
    private String genderPreferred;

    private String isType;

    private ImageDTO[] images;
    private CountryDTO[] countriesPreferences;

    private String street;
    private String zipCode;
    private String city;
    private String province;
    private String phone;
}
