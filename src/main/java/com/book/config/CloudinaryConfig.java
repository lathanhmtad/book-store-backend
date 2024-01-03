package com.book.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dixswfj8d",
                "api_key", "876943471796471",
                "api_secret", "XXlwrvw541Qmp83wIgDjxZpIoNQ",
                "secure", true
        ));
    }
}

