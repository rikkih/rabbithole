package com.hoderick.rabbithole.user;

import com.hoderick.rabbithole.s3.S3ClientConfig;
import com.hoderick.rabbithole.s3.S3StorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Optional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class UserProfileService {

    private final AvatarFileValidator avatarFileValidator;
    private final AvatarKeyGenerator avatarKeyGenerator;
    private final UserProfileRepository userProfileRepository;
    private final S3ClientConfig s3ClientConfig;
    private final S3StorageService s3StorageService;
    private final UserProfileMapper userProfileMapper;

    public UserProfileDto getOrCreateProfile() {
        UserProfile user = getCurrentUser();
        return userProfileMapper.toDto(user);
    }

    public URL getAvatarUrl() {
        UserProfile user = getCurrentUser();
        return s3StorageService.generatePresignedUrl(s3ClientConfig.getAvatarBucketName(), user.getAvatarKey());
    }

    @Transactional
    public void updateAvatar(MultipartFile file) {
        avatarFileValidator.validate(file);
        String userId = getCurrentUserIdFromSecurityContext();

        // TODO: Change this to a NotFoundException.
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        String avatarKey = avatarKeyGenerator.generate(userId, file.getOriginalFilename());
        s3StorageService.upload(s3ClientConfig.getAvatarBucketName(), avatarKey, file);
        user.setAvatarKey(avatarKey);
    }

    private UserProfile getCurrentUser() {
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

    /**
     * We get the User ID from the Authentication.name field. This is what is used as the UserProfile.id identifier.
     * @return userId
     */
    private String getCurrentUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return Optional.ofNullable(authentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName) // Auth0 sub
                .orElseThrow(() -> new AccessDeniedException("User is not authenticated"));
    }
}
