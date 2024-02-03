package com.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE category SET deleted = true WHERE id=?")
@Where(clause = "deleted=false or deleted is null")
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
