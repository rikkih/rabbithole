package com.hoderick.rabbithole.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class S3StorageServiceImpl implements S3StorageService {

    private final S3Client s3Client;

    @Override
    public void upload(String bucket, String key, MultipartFile file) {
        ensureBucketExists(bucket);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to upload Avatar.", ioe);
        }
    }

    @Override
    public void delete(String bucket, String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

    @Override
    public URL generatePresignedUrl(String bucket, String key) {
        return s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucket).key(key));
    }

    private void ensureBucketExists(String bucket) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (NoSuchBucketException e) {
            // TODO: Do we want to create a bucket if it doesn't exist?
            // We might want to just ensure the given bukcets exist? Fail safe
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
        }
    }
}
