package com.aupair.aupaircl.service.approvalservice;

import com.aupair.aupaircl.model.aupairprofile.AuPairProfile;
import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfile;
import com.aupair.aupaircl.model.hostfamilyprofile.HostFamilyProfileRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
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
    private final UserRepository userRepository;
    public ApprovalService(ProfileRepository profileRepository, AuPairProfileRepository auPair,
                           HostFamilyProfileRepository hostFamilyProfileRepository,UserRepository userRepository){
        this.profileRepository = profileRepository;
        this.auPairProfileRepository = auPair;
        this.hostFamilyProfileRepository = hostFamilyProfileRepository;
        this.userRepository = userRepository;
    }

    public void approveProfileSection(UUID profileId, Boolean isApproved) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if (profile.isPresent()) {
            profile.get().setIsApproved(isApproved);
            profileRepository.saveAndFlush(profile.get());
        }
    }

    public void approveAuPairProfileSection(UUID auPairProfileId,boolean isApproved) {
        Optional<AuPairProfile> auPairProfile = auPairProfileRepository.findById(auPairProfileId);
        if (auPairProfile.isPresent()) {
            auPairProfile.get().setIsApproved(isApproved);
            auPairProfileRepository.saveAndFlush(auPairProfile.get());
        }
    }

    public void approveHostFamilyProfileSection(UUID hostFamilyProfileId,boolean isApproved) {
        Optional<HostFamilyProfile> hostFamilyProfile = hostFamilyProfileRepository.findById(hostFamilyProfileId);
        if (hostFamilyProfile.isPresent()){
            hostFamilyProfile.get().setIsApproved(isApproved);
            hostFamilyProfileRepository.saveAndFlush(hostFamilyProfile.get());
        }
    }
    public void approveAccount(UUID account, boolean isApproved){
       Optional<User> user = this.userRepository.findById(account);
       if (user.isPresent()){
           user.get().setIsLocked(isApproved);
           this.userRepository.saveAndFlush(user.get());
       }
    }
}
