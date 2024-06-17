package com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class FindHostFamilyDashboardDto {
    private String auPairCountry;
    private String gender;
    private String locationType;
    private List<String> preferredCountryIds;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private int minDuration;
    private int maxDuration;
    private int childrenAgeMin;
    private int childrenAgeMax;
}
