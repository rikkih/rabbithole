package com.hoderick.rabbithole.s3;

import java.io.InputStream;

public interface S3StorageService {
    void upload(String bucket, String key, InputStream inputStream, long contentLength);
    void delete(String bucket, String key);
}
