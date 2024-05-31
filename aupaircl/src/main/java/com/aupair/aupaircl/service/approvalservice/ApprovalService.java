package com.aupair.aupaircl.service.approvalservice;

import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public void approveProfileSection(UUID profileId) {
        Profile profile = profileRepository.findById(profileId).get();
        profile.setIsApproved(true);
        profileRepository.save(profile);
    }

    public void approveAuPairProfileSection(UUID auPairProfileId) {
        AuPairProfile auPairProfile = auPairProfileRepository.findById(auPairProfileId).get();
        auPairProfile.setIsApproved(true);
        auPairProfileRepository.save(auPairProfile);
    }

    public void approveHostFamilyProfileSection(UUID hostFamilyProfileId) {
        HostFamilyProfile hostFamilyProfile = hostFamilyProfileRepository.findById(hostFamilyProfileId).get();
        hostFamilyProfile.setIsApproved(true);
        hostFamilyProfileRepository.save(hostFamilyProfile);
    }
}
