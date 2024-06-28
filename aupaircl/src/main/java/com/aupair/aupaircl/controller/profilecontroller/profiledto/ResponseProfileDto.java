package com.aupair.aupaircl.controller.profilecontroller.profiledto;

import com.aupair.aupaircl.model.image.Image;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ResponseProfileDto {
    private String name;
    private String lastname;
    private String gender;
    private Integer age;
    private String country;
    private String nationality;
    private String languageOur;
    private String languageOther;
    @JsonFormat(pattern="yyyy-MM")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM")
    private Date endDate;
    private Integer minStayMonths;
    private Integer maxStayMonths;
    private String aboutMe;
    private List<CountryDTO> preferredCountries;
    private String locationType;
    private String preferredRegion;
    private Integer childrenAgeMin;
    private Integer childrenAgeMax;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLogin;
    private List<Image> images;
}
