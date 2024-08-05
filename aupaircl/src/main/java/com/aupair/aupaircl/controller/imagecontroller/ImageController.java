package com.aupair.aupaircl.controller.imagecontroller;

import com.aupair.aupaircl.controller.imagecontroller.imagedto.ImageUpdateDTO;
import com.aupair.aupaircl.model.user.UserEmailDto;
import com.aupair.aupaircl.service.imageservice.ImageService;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/images")
@CrossOrigin(value = "http://localhost:5173/")
@Slf4j
public class ImageController {
    private final ImageService imageService;
    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
    @PutMapping(value = "/update",produces = "application/json")
    public ResponseEntity<CustomResponse> updateProfileImages(@RequestBody ImageUpdateDTO[] imageDTOs) {
        try {
            return imageService.updateProfileImage(imageDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.OK.value(), "Algo ocurrio al actualziar imagenes"));
        }
    }
    @PostMapping(value = "/getImageToHeader",produces = "application/json")
    public ResponseEntity<CustomResponse> getImageToHeaderProfile(@RequestBody UserEmailDto userEmailDto) {
        try {
            return this.imageService.findImagesByProfile(userEmailDto.getEmail());
        }catch (Exception e) {
            log.error("Algo sucedio al obtener imagen de profile");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal al obtener imagen de profile"));
        }

    }
}
