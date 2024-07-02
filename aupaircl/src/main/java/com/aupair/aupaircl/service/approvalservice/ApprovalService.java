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
            profile.get().setApproved(isApproved);
            profileRepository.saveAndFlush(profile.get());
        }
    }

    public void approveAuPairProfileSection(UUID auPairProfileId,boolean isApproved) {
        Optional<AuPairProfile> auPairProfile = auPairProfileRepository.findById(auPairProfileId);
        Optional<User> user = this.userRepository.findByEmail(auPairProfile.get().getUser().getEmail());
        if (user.isPresent() && !isApproved) {
            user.get().setLocked(true);
            this.userRepository.saveAndFlush(user.get());
        }
        if (auPairProfile.isPresent()) {
            auPairProfile.get().setApproved(isApproved);
            auPairProfileRepository.saveAndFlush(auPairProfile.get());
        }
    }

    public void approveHostFamilyProfileSection(UUID hostFamilyProfileId,boolean isApproved) {
        Optional<HostFamilyProfile> hostFamilyProfile = hostFamilyProfileRepository.findById(hostFamilyProfileId);
        Optional<User> user = this.userRepository.findByEmail(hostFamilyProfile.get().getUser().getEmail());
        if (user.isPresent() && !isApproved) {
            user.get().setLocked(true);
            this.userRepository.saveAndFlush(user.get());
        }
        if (hostFamilyProfile.isPresent()){
            hostFamilyProfile.get().setApproved(isApproved);
            hostFamilyProfileRepository.saveAndFlush(hostFamilyProfile.get());
        }
    }
    public void approveAccount(UUID account, boolean isApproved){
       Optional<User> user = this.userRepository.findById(account);
       if (user.isPresent()){
           if (user.get().getFailedAttempts()>1){
               user.get().setFailedAttempts(0);
           }
           user.get().setLocked(isApproved);
           this.userRepository.saveAndFlush(user.get());
       }
    }
}
