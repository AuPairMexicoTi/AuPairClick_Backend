package com.aupair.aupaircl.service.imageservice;

import com.aupair.aupaircl.controller.imagecontroller.imagedto.ImageUpdateDTO;
import com.aupair.aupaircl.model.image.Image;
import com.aupair.aupaircl.model.image.ImageRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import com.aupair.aupaircl.service.imageservice.mapperimage.MapperImage;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImageService {
private static ImageRepository imageRepository;
private static ProfileRepository profileRepository;
    private final MapperImage mapperImage;

    @Autowired
public ImageService(ImageRepository imageRepository, ProfileRepository profileRepository, MapperImage mapperImage) {
this.imageRepository = imageRepository;
this.profileRepository = profileRepository;
        this.mapperImage = mapperImage;
    }

    @Transactional
    public ResponseEntity<CustomResponse> updateProfileImage(ImageUpdateDTO[] imageDTO) {
        try {
            Profile profile = profileRepository.findByUser_EmailAndIsApproved(imageDTO[0].getEmail(), true);
            if (profile == null) {
                log.error("Could not find user");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "Usuario invalido"));
            }

            // Mapear los DTOs de imagen a entidades Image
            List<Image> selectedImages = mapperImage.mapImageFromImageDto(imageDTO);

            // Obtener las imágenes actuales del perfil
            List<Image> currentImages = imageRepository.findByProfile_User_EmailAndProfile_IsApproved(profile.getUser().getEmail(), true);

            // Crear listas para identificar las imágenes a eliminar y agregar
            List<Image> imagesToRemove = new ArrayList<>();
            List<Image> imagesToAdd = new ArrayList<>(selectedImages);

            // Comparar las imágenes actuales con las nuevas
            for (Image currentImage : currentImages) {
                boolean existsInSelected = selectedImages.stream().anyMatch(image -> image.getImageName().equals(currentImage.getImageName()));
                if (!existsInSelected) {
                    imagesToRemove.add(currentImage);
                } else {
                    imagesToAdd.removeIf(image -> image.getImageName().equals(currentImage.getImageName()));
                }
            }

            // Eliminar las imágenes que ya no están presentes
            imageRepository.deleteAll(imagesToRemove);

            // Agregar las nuevas imágenes que no existen en las actuales
            for (Image imageToAdd : imagesToAdd) {
                Image imageSaved = new Image();
                imageSaved.setImageName(imageToAdd.getImageName());
                imageSaved.setImageLabel(imageToAdd.getImageLabel());
                imageSaved.setProfile(profile);
                imageRepository.saveAndFlush(imageSaved);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), "Actualizado"));
        } catch (Exception e) {
            log.error("Algo salio mal al actualizar imagenes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal"));
        }
    }

}
