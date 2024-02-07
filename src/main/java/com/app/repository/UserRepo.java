package com.app.repository;

import com.app.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends AbstractBaseRepo<User> {
    Optional<User> findByEmail(String email);
}
