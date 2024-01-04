package com.book.config;

import com.book.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@AllArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {
    private UserRepo userRepo;
    @Override
    public Optional<Long> getCurrentAuditor() {
        Long currentUserId = userRepo.findIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return Optional.ofNullable(currentUserId);
    }
}