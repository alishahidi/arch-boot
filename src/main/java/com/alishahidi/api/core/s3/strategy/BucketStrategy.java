package com.alishahidi.api.core.s3.strategy;

import com.alishahidi.api.core.s3.exception.BucketPutException;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface BucketStrategy {

    CompletableFuture<String> put(S3AsyncClient client, String bucketName, String key, Path path) throws BucketPutException;
}
