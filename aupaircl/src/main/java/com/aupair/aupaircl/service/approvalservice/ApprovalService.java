package com.aupair.aupaircl.service.approvalservice;

import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
public class ApprovalService {


    private final ProfileRepository profileRepository;
    private final AuPairProfileRepository auPairProfileRepository;
    private final HostFamilyProfileRepository hostFamilyProfileRepository;
    public ApprovalService(ProfileRepository profileRepository, AuPairProfileRepository auPair,HostFamilyProfileRepository hostFamilyProfileRepository){
        this.profileRepository = profileRepository;
        this.auPairProfileRepository = auPair;
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
    }

    public void approveProfileSection(UUID profileId, boolean isApproved) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if (profile.isPresent()) {
            profile.get().setIsApproved(isApproved);
            profileRepository.save(profile.get());
        }
    }

    public void approveAuPairProfileSection(UUID auPairProfileId,boolean isApproved) {
        AuPairProfile auPairProfile = auPairProfileRepository.findById(auPairProfileId).get();
        auPairProfile.setIsApproved(isApproved);
        auPairProfileRepository.save(auPairProfile);
    }

    public void approveHostFamilyProfileSection(UUID hostFamilyProfileId,boolean isApproved) {
        HostFamilyProfile hostFamilyProfile = hostFamilyProfileRepository.findById(hostFamilyProfileId).get();
        hostFamilyProfile.setIsApproved(isApproved);
        hostFamilyProfileRepository.save(hostFamilyProfile);
    }
}
