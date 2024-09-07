package com.alishahidi.api.core.s3;

import com.alishahidi.api.core.s3.config.S3ClientConfig;
import com.alishahidi.api.core.s3.strategy.BucketStrategy;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Bucket {

    S3AsyncClient client;
    BucketStrategy strategy;
    String name;

    private Bucket(S3AsyncClient client, BucketStrategy strategy, String name) {
        this.client = client;
        this.strategy = strategy;
        this.name = name;
    }

    public static BucketBuilder builder() {
        return new BucketBuilder();
    }

    public static class BucketBuilder {
        private S3ClientConfig config;
        private BucketStrategy strategy;
        private String name;

        public BucketBuilder config(S3ClientConfig config) {
            this.config = config;
            return this;
        }

        public BucketBuilder strategy(BucketStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public BucketBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Bucket build() {
            if (config == null || strategy == null || name == null) {
                throw new IllegalStateException("Config, Strategy, and Name must be provided");
            }
            S3AsyncClient client = S3ClientFactory.create(config);
            return new Bucket(client, strategy, name);
        }
    }

    public CompletableFuture<String> put(String key, Path path) {
        return strategy.put(client, name, key, path)
                .thenApply(s3Key -> {
                    return s3Key;
                })
                .exceptionally(ex -> {
                    return null;
                });
    }
}
