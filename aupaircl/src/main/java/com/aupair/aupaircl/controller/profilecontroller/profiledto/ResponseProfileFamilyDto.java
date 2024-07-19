package com.aupair.aupaircl.controller.profilecontroller.profiledto;

import com.aupair.aupaircl.model.image.Image;
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
    private String numPerfil;
    private String aboutMe;
    private boolean aupairExp;
    private boolean areSingleFamily;
    private boolean aupairCareChildrenNeed;
    private boolean smokesInFamily;
    private boolean havePets;
    private boolean aupairSmoker;
    private boolean aupairDrivingLicense;
    private boolean aupairHouseWork;
    private Integer aupairAgeMin;
    private Integer aupairAgeMax;
    private String aupairLanguageOurOther;
    private String aupairLanguageOther;
    private List<Image> images;
    private List<CountryDTO> preferredCountries;
}
