package com.alishahidi.api.core.s3.strategy;

import com.alishahidi.api.core.s3.exception.BucketPutException;
import com.alishahidi.api.core.util.IOUtils;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class StandardBucketStrategy implements BucketStrategy {

    public CompletableFuture<String> put(S3AsyncClient client, String bucketName, String key, Path path) throws BucketPutException {
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return client.putObject(putOb, AsyncRequestBody.fromFile(path.toFile()))
                .thenApply(res -> {
                    IOUtils.deleteFile(path);
                    return key;
                })
                .exceptionally(e -> {
                    throw new BucketPutException("Error uploading file: " + e.getMessage(), e);
                });
    }
}
