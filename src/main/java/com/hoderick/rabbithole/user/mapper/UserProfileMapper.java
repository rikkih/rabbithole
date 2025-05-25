package com.hoderick.rabbithole.user.mapper;

import com.hoderick.rabbithole.s3.config.S3ClientConfig;
import com.hoderick.rabbithole.s3.service.S3StorageService;
import com.hoderick.rabbithole.user.dto.UserProfileDto;
import com.hoderick.rabbithole.user.model.UserProfile;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileMapper {

    private final S3ClientConfig s3ClientConfig;
    private final S3StorageService s3StorageService;

    public UserProfileDto toDto(UserProfile userProfile) {
        String avatarBucket = s3ClientConfig.getAvatarBucketName();
        String avatarKey = userProfile.getAvatarKey() == null ? null : userProfile.getAvatarKey();

        URL presignedAvatarUrl = null;
        if (avatarKey != null && !avatarKey.isBlank()) {
            presignedAvatarUrl = s3StorageService.generatePresignedUrl(avatarBucket, avatarKey);
        }

        return new UserProfileDto(userProfile.getId(),
                userProfile.getDisplayName(),
                userProfile.getEmail(),
                userProfile.getBio(),
                presignedAvatarUrl != null ? presignedAvatarUrl.toString() : null);
    }
}
