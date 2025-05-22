package com.hoderick.rabbithole.s3;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface S3StorageService {
    void delete(String bucket, String key);
    URL generatePresignedUrl(String bucket, String key);
    void upload(String bucket, String key, MultipartFile file);
}
