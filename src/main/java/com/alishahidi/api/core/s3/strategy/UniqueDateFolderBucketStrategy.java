package com.alishahidi.api.core.s3.strategy;

import com.alishahidi.api.core.s3.exception.BucketPutException;
import com.alishahidi.api.core.util.IOUtils;
import com.alishahidi.api.core.util.StringUtils;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UniqueDateFolderBucketStrategy implements BucketStrategy {

    public CompletableFuture<String> put(S3AsyncClient client, String bucketName, String key, Path path, String folderName) throws BucketPutException {
        key = folderName + "/" + dateUniqueName(key);

        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        String finalKey = key;
        return client.putObject(putOb, AsyncRequestBody.fromFile(path.toFile()))
                .thenApply(res -> {
                    IOUtils.deleteFile(path);
                    return finalKey;
                })
                .exceptionally(e -> {
                    throw new BucketPutException("Error uploading file: " + e.getMessage(), e);
                });
    }

    private String dateUniqueName(String name) {
        // Sanitize the original file name
        String sanitizedFileName = StringUtils.normalize(name);

        // Create a timestamp string with the desired format
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        // Optionally add a UUID or random string to ensure uniqueness
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        // Combine timestamp, uniqueId, and sanitized file name
        return timestamp + "_" + uniqueId + "_" + sanitizedFileName;
    }
}
