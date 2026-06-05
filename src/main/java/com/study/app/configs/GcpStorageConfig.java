package com.study.app.configs;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Configuration
public class GcpStorageConfig {
	
    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials =
                GoogleCredentials.fromStream(
                        new ClassPathResource("GCP-festaroute.json")
                                .getInputStream()
                );

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
