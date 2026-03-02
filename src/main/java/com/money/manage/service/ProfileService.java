package com.money.manage.service;

import com.money.manage.dto.AuthDTO;
import com.money.manage.dto.ProfileDTO;
import com.money.manage.entity.ProfileEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface ProfileService {

    ProfileDTO registerProfile(ProfileDTO profileDTO);
    Boolean activateProfile(String activationToken);
    Boolean isProfileActive(String email);
    ProfileEntity getCurrentProfile();
    ProfileDTO getPublicProfile(String email);
    Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO);
}
