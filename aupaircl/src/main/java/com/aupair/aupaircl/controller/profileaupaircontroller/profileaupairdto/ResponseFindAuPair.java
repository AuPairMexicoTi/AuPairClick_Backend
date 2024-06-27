package com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ResponseFindAuPair {
    private String nameAuPair;
    private String image;
    private String location;
    private Integer childrenAgeMin;
    private Integer childrenAgeMax;
    @JsonFormat(pattern="yyyy-MM")
    private Date availableFrom;
    @JsonFormat(pattern="yyyy-MM")
    private Date availableTo;
    private Integer minStayMonths;
    private Integer maxStayMonths;
    private String description;
}
