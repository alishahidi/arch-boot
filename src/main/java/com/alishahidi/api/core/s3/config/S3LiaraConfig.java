package com.alishahidi.api.core.s3.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "s3.liara")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class S3LiaraConfig implements S3ClientConfig {

    String endpoint;
    String access;
    String secret;
}
