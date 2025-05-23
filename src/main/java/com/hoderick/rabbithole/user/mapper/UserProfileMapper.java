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
        String avatarKey = userProfile.getAvatarKey();
        String avatarBucket = s3ClientConfig.getAvatarBucketName();
        URL presignedAvatarUrl = s3StorageService.generatePresignedUrl(avatarBucket, avatarKey);

        return new UserProfileDto(userProfile.getDisplayName(),
                userProfile.getEmail(),
                userProfile.getBio(),
                presignedAvatarUrl.toString());
    }
}
