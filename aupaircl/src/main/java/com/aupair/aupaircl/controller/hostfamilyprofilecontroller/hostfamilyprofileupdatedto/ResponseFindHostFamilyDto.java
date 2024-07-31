package com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ResponseFindHostFamilyDto {
    private String nameHost;
    private String image;
    private String location;
    private Integer numberOfChildren;
    private Integer childrenAgeMin;
    private Integer childrenAgeMax;
    @JsonFormat(pattern="yyyy-MM")
    private Date searchFrom;
        @JsonFormat(pattern="yyyy-MM")
        private Date searchTo;
    private Integer minStayMonths;
    private Integer maxStayMonths;
    private String description;
    private String numPerfilFamily;
}
