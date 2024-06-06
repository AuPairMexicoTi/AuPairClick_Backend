package com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto;

import com.aupair.aupaircl.controller.profilecontroller.profiledto.CountryDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
public class FamilyProfileUpdateDTO {
    private String email;
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
private CountryDTO[] countries;
private String lada;
}
