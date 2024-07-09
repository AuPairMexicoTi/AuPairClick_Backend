package com.aupair.aupaircl.controller.profilecontroller.profiledto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseProfileFamilyDto {
    private String name;
    private String lastname;
    private String surname;
    private String country ;
    private String nationality;
    private Integer age;
    private String genderPreferred;
    private Integer minStayMonths;
    private Integer maxStayMonths;
    @JsonFormat(pattern="yyyy-MM")
    private Date searchFrom;
    @JsonFormat(pattern="yyyy-MM")
    private Date searchTo;
    private Integer numOfChildren;
    private Integer childrenAgeMin;
    private Integer childrenAgeMax;
    private String locationType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLogin;
    private String aboutMe;
    private List<CountryDTO> preferredCountries;
}
