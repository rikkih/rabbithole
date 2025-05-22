package com.hoderick.rabbithole.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
class UserProfileMapper {

    private final String avatarBucketBaseUrl;

    UserProfileMapper(@Qualifier("avatarBucketBaseUrl") String avatarBucketBaseUrl) {
        this.avatarBucketBaseUrl = avatarBucketBaseUrl;
    }

    UserProfileDto toDto(UserProfile userProfile) {
        String avatarKey = userProfile.getAvatarKey();
        String avatarUrl = (avatarKey != null) ?
                avatarBucketBaseUrl + "/" + avatarKey :
                null;

        return new UserProfileDto(userProfile.getDisplayName(), userProfile.getEmail(), userProfile.getBio(), avatarUrl);
    }
}
