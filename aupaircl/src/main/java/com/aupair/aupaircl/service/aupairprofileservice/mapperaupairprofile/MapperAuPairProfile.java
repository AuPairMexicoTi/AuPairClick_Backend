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
    public static ResponseFindAuPair mapAuPairProfileToAupairProfileDTO(AuPairProfile hostFamilyProfile){
        ResponseFindAuPair responseFindAuPair = new ResponseFindAuPair();
        responseFindAuPair.setNameAuPair(hostFamilyProfile.getUser().getProfile().getFirstName());
        responseFindAuPair.setDescription(hostFamilyProfile.getUser().getProfile().getAboutMe());
        responseFindAuPair.setLocation(hostFamilyProfile.getUser().getProfile().getCountry().getCountryName());
        responseFindAuPair.setAvailableFrom(hostFamilyProfile.getAvailableFrom());
        responseFindAuPair.setAvailableTo(hostFamilyProfile.getAvailableTo());
        responseFindAuPair.setChildrenAgeMin(hostFamilyProfile.getChildrenAgeMinSearch());
        responseFindAuPair.setChildrenAgeMax(hostFamilyProfile.getChildrenAgeMaxSearch());
        List<Image> images = imageRepository.findByProfile_User_EmailAndProfile_IsApproved(hostFamilyProfile.getUser().getEmail(),true);
        responseFindAuPair.setImage(images.get(0).getImageName());
        responseFindAuPair.setMinStayMonths(hostFamilyProfile.getUser().getProfile().getMinStayMonths());
        responseFindAuPair.setMaxStayMonths(hostFamilyProfile.getUser().getProfile().getMaxStayMonths());
        return responseFindAuPair;
    }
}
