package com.book.config;

import com.book.security.MyUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@AllArgsConstructor
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfig {
    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            return Optional.of(userDetails.getId());
        };
    }
}
