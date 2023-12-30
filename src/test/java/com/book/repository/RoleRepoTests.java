package com.book.repository;

import com.book.entity.Privilege;
import com.book.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Collection;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepoTests {
    @Autowired
    RoleRepo roleRepo;

    @Autowired
    PrivilegeRepo privilegeRepo;
    @Test
    public void createFirstRole() {
        Privilege read = Privilege.builder()
                        .name("Read").build();

        Privilege create = Privilege.builder()
                .name("Create").build();

        Privilege update = Privilege.builder()
                .name("Update").build();

        Privilege delete = Privilege.builder()
                .name("Delete").build();
        Collection<Privilege> privileges = List.of(read, create, update, delete);

        privilegeRepo.saveAll(privileges);

        Role admin = Role.builder()
                .name("ROLE_AdminFullAccess")
                .description("full access")
                .privileges(privileges)
                .build();
        Role adminSaved = roleRepo.save(admin);

        System.out.println(adminSaved);
    }
}
