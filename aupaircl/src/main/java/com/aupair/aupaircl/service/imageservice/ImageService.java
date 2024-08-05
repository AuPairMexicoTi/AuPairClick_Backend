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
private final ImageRepository imageRepository;
private final ProfileRepository profileRepository;
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
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> findImagesByProfile(String email) {
        try {
            List<Image> imageList = this.imageRepository.findByProfile_User_EmailAndProfile_IsApproved(email,true);
            if (imageList.isEmpty()){
                log.error("No hay imagenes de perfil");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("No hay imagenes para este perfil",HttpStatus.OK.value(), false, "https://media.istockphoto.com/id/1171169127/es/foto/disparo-de-cabeza-de-hombre-guapo-alegre-con-corte-de-pelo-de-moda-y-gafas-aisladas-en-el.jpg?s=612x612&w=0&k=20&c=5l5tRTmRQHFFHAZZjgpIiOUY-6HHbzwuV74mcW4z_Mw="));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Imagenes encontradas", HttpStatus.OK.value(), false, imageList.get(0).getImageName()));
        }catch (Exception e){
            log.error("Error al obtener imagenes: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal al obtener las imagenes"));
        }
    }
}
