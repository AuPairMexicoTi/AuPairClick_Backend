package com.aupair.aupaircl.service.imageservice.mapperimage;

import com.aupair.aupaircl.controller.imagecontroller.imagedto.ImageUpdateDTO;
import com.aupair.aupaircl.model.image.Image;
import com.aupair.aupaircl.model.image.ImageRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MapperImage {
    private static ImageRepository imageRepository;
    private final ProfileRepository profileRepository;

    @Autowired
public MapperImage(ImageRepository imageRepository, ProfileRepository profileRepository){
    this.imageRepository=imageRepository;
        this.profileRepository = profileRepository;
    }
public List<Image> mapImageFromImageDto(ImageUpdateDTO[]imageDto){
return Arrays.stream(imageDto)
        .map(dto -> {
            Image image = new Image();
            image.setImageName(dto.getUrlImage());
            image.setImageLabel(dto.getLabel());
            Optional<Profile> profile = profileRepository.findByUser_Email(dto.getEmail());
            image.setProfile(profile.get());
            return image;
        })
        .collect(Collectors.toList());
}
}
