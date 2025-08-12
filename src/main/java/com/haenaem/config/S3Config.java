package com.haenaem.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Slf4j
public class S3Config {

    @Value("${haenaem.storage.s3.access-key}")
    private String accessKey;

    @Value("${haenaem.storage.s3.secret-key}")
    private String secretKey;

    @Value("${haenaem.storage.s3.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        log.info("S3Client Bean 생성 - Region: {}", region);
        
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .build();
    }
}