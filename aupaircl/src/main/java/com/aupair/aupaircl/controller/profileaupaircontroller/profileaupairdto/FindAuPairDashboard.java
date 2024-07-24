package com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FindAuPairDashboard {
    private String familyCountry;
    private String genderSearch;
    private List<String> preferredCountryNames;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private int minDuration;
    private int maxDuration;
    private String languageOurOther;
    private String languageOther;
    private boolean smoker;
    private boolean drivingLicence;
    private boolean houseWork;
}
