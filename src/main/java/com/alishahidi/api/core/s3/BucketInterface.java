package com.alishahidi.api.core.s3;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

public interface BucketInterface {

    void put(S3Client client, String bucketName, String key, MultipartFile file);
}
