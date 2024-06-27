package com.aupair.aupaircl.service.hostfamilyprofileservice.mapperhostprofile;

import com.aupair.aupaircl.controller.hostfamilyprofilecontroller.hostfamilyprofileupdatedto.ResponseFindHostFamilyDto;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.image.Image;
import com.aupair.aupaircl.model.image.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapperHostProfile {

    private static ImageRepository imageRepository;

    public MapperHostProfile(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    public static List<ResponseFindHostFamilyDto> mapHostToResponseProfile(List<HostFamilyProfile> orderList) {
        return orderList.stream()
                .map(MapperHostProfile::mapHostProfileToHostProfileDTO).toList();
    }
    public static ResponseFindHostFamilyDto mapHostProfileToHostProfileDTO(HostFamilyProfile hostFamilyProfile){
        ResponseFindHostFamilyDto responseFindHostFamilyDto = new ResponseFindHostFamilyDto();
        responseFindHostFamilyDto.setNameHost(hostFamilyProfile.getUser().getProfile().getFirstName());
        responseFindHostFamilyDto.setDescription(hostFamilyProfile.getUser().getProfile().getAboutMe());
        responseFindHostFamilyDto.setNumberOfChildren(hostFamilyProfile.getNumberOfChildren());
        responseFindHostFamilyDto.setLocation(hostFamilyProfile.getUser().getProfile().getCountry().getCountryName());
        responseFindHostFamilyDto.setSearchFrom(hostFamilyProfile.getSearchFrom());
        responseFindHostFamilyDto.setSearchTo(hostFamilyProfile.getSearchTo());
        responseFindHostFamilyDto.setChildrenAgeMin(hostFamilyProfile.getChildrenAgesMin());
        responseFindHostFamilyDto.setChildrenAgeMax(hostFamilyProfile.getChildrenAgesMax());
        List<Image> images = imageRepository.findByProfile_User_EmailAndProfile_IsApproved(hostFamilyProfile.getUser().getEmail(),true);
        responseFindHostFamilyDto.setImage(images.get(0).getImageName());
        responseFindHostFamilyDto.setMinStayMonths(hostFamilyProfile.getUser().getProfile().getMinStayMonths());
        responseFindHostFamilyDto.setMaxStayMonths(hostFamilyProfile.getUser().getProfile().getMaxStayMonths());
        return responseFindHostFamilyDto;
    }
}
