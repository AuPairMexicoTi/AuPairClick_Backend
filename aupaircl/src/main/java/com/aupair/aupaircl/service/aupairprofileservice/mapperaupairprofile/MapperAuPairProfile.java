package com.aupair.aupaircl.service.aupairprofileservice.mapperaupairprofile;

import com.aupair.aupaircl.controller.profileaupaircontroller.profileaupairdto.ResponseFindAuPair;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.image.Image;
import com.aupair.aupaircl.model.image.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapperAuPairProfile {

    private static ImageRepository imageRepository;

    public MapperAuPairProfile(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    public static List<ResponseFindAuPair> mapAuPairToResponseProfile(List<AuPairProfile> orderList) {
        return orderList.stream()
                .map(MapperAuPairProfile::mapAuPairProfileToAupairProfileDTO).toList();
    }
    public static ResponseFindAuPair mapAuPairProfileToAupairProfileDTO(AuPairProfile auPairProfile){
        ResponseFindAuPair responseFindAuPair = new ResponseFindAuPair();
        responseFindAuPair.setNameAuPair(auPairProfile.getUser().getProfile().getFirstName());
        responseFindAuPair.setDescription(auPairProfile.getUser().getProfile().getAboutMe());
        responseFindAuPair.setLocation(auPairProfile.getUser().getProfile().getCountry().getCountryName());
        responseFindAuPair.setAvailableFrom(auPairProfile.getAvailableFrom());
        responseFindAuPair.setAvailableTo(auPairProfile.getAvailableTo());
        responseFindAuPair.setChildrenAgeMin(auPairProfile.getChildrenAgeMinSearch());
        responseFindAuPair.setChildrenAgeMax(auPairProfile.getChildrenAgeMaxSearch());
        List<Image> images = imageRepository.findByProfile_User_EmailAndProfile_IsApproved(auPairProfile.getUser().getEmail(),true);
        responseFindAuPair.setImage(images.get(0).getImageName());
        responseFindAuPair.setMinStayMonths(auPairProfile.getUser().getProfile().getMinStayMonths());
        responseFindAuPair.setMaxStayMonths(auPairProfile.getUser().getProfile().getMaxStayMonths());
        responseFindAuPair.setNumPerfil(auPairProfile.getUser().getProfile().getNumPerfil());
        return responseFindAuPair;
    }
}
