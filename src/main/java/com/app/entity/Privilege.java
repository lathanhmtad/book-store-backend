package com.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.Collection;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Privilege extends BaseEntity{

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
}
