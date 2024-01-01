package com.book.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    private final String CLOUD_NAME = "dixswfj8d";
    private final String API_KEY = "876943471796471";
    private final String API_SECRET = "XXlwrvw541Qmp83wIgDjxZpIoNQ";

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET,
                "secure", true
        ));
        return cloudinary;
    }
}

