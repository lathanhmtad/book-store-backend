package com.book.config;

import com.book.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
@AllArgsConstructor
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {
    private UserRepo userRepo;
    @Bean
    AuditorAware<Long> auditorProvider() {
        return () -> {
            Long currentUserId = userRepo.findIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            return Optional.ofNullable(currentUserId);
        };
    }
}
