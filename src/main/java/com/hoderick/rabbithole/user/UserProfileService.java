package com.hoderick.rabbithole.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    UserProfile getOrCreateProfile() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getSubject();

        return userProfileRepository.findById(userId)
                .orElseGet(() -> {
                    UserProfile profile = new UserProfile(userId);
                    profile.setDisplayName(jwt.getClaimAsString("name"));
                    profile.setEmail(jwt.getClaimAsString("email"));
                    return userProfileRepository.save(profile);
                });
    }
}
