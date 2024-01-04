package com.book.config;

import com.book.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@AllArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfig {
    private UserRepo userRepo;
    @Bean
    public AuditorAware<Long> auditorAware() {
        return new AuditorAwareImpl(userRepo);
    }
}
