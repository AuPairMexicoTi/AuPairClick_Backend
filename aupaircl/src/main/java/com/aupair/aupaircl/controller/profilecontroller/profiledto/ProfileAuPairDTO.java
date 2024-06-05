package com.aupair.aupaircl.controller.profilecontroller.profiledto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ProfileAuPairDTO {
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date available_from;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date available_to;
    private String child_care_experience;
    private String motivation;
    private Boolean smoke;
    private String gender;
}
