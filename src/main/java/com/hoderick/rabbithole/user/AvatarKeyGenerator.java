package com.hoderick.rabbithole.user;

import com.hoderick.rabbithole.s3.S3ClientConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class AvatarKeyGenerator {

    private final S3ClientConfig s3ClientConfig;

    private static final String DEFAULT_EXTENSION = ".jpg";

    String generate(String userId, String originalFilename) {
        String extension = Optional.ofNullable(originalFilename)
                .filter(name -> name.contains("."))
                .map(name -> name.substring(name.lastIndexOf('.')))
                .orElse(DEFAULT_EXTENSION);

        return s3ClientConfig.getAvatarBucketName() + "/" + userId + "_" + UUID.randomUUID() + extension;
    }
}
