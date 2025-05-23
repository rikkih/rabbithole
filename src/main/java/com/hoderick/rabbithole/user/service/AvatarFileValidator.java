package com.hoderick.rabbithole.user.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
class AvatarFileValidator {

    private static final long MAX_BYTE_SIZE = 5 * 1024 * 1024; // 5mb
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/png", "image/jpeg", "image/jpg");

    void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > MAX_BYTE_SIZE) {
            throw new IllegalArgumentException("File size exceeds limit of 5MB");
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Invalid file type. Allowed: PNG, JPEG");
        }
    }
}
