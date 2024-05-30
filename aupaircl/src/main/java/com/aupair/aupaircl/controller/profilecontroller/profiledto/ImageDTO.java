package com.aupair.aupaircl.controller.profilecontroller.profiledto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class ImageDTO {
    private String imageLabel;
    private String imageName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String profile;
}
