package com.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category extends BaseEntity {
    @Column(unique = true)
    private String name;

    private String image;
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children;
}
