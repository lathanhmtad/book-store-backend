package com.app.entity;

import com.app.entity.listener.BrandListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@EntityListeners(BrandListener.class)
public class Brand extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    private String logo;

    @ManyToMany
    @JoinTable(
            name = "brand_category",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();


    public List<String> getCategoryNames() {
        return this.categories.stream().map(Category::getName).collect(Collectors.toList());
    }
}
