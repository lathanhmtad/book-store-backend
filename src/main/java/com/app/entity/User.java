package com.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class User extends BaseEntity {
    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;
    private Boolean enabled;
    private String photo;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @Builder
    public User(Long id, String fullName, String email, String password,
                Boolean enabled, String photo, Collection<Role> roles) {
        super(id);
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.photo = photo;
        this.roles = roles;
    }

    public User(Long id) {
        super(id);
    }
}
