package com.book.repository;

import com.book.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepoTests {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateRootUser() {
        Rol
        User admin = User.builder()
                .fullName("Administrator")
                .email("admin@gmail.com")
                .password("admin")
                .enabled(true)
                .photo("admin1.jpg")
                .roles()
                .build();
    }
}
