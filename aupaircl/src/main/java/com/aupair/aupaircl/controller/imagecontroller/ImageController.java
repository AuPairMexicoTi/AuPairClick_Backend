package com.aupair.aupaircl.controller.imagecontroller;

import com.aupair.aupaircl.controller.imagecontroller.imagedto.ImageUpdateDTO;
import com.aupair.aupaircl.service.imageservice.ImageService;
import com.aupair.aupaircl.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/images")
@CrossOrigin(value = "http://localhost:5173/")
public class ImageController {
    private static ImageService imageService;
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
}
