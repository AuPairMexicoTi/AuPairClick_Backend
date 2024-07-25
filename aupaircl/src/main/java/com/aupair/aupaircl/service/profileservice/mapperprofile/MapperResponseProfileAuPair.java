package com.aupair.aupaircl.service.profileservice.mapperprofile;

import com.aupair.aupaircl.model.aupairprofile.AuPairProfileRepository;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class MapperResponseProfileAuPair {
    private static ProfileRepository profileRepository;
    private static AuPairProfileRepository auPairProfileRepository;
    public MapperResponseProfileAuPair(ProfileRepository profileRepository, AuPairProfileRepository auPairProfileRepository){
        this.profileRepository = profileRepository;
        this.auPairProfileRepository = auPairProfileRepository;
    }
}
