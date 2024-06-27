package com.aupair.aupaircl.controller.imagecontroller.imagedto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ImageUpdateDTO {
private String email;
private String urlImage;
private String label;
}
