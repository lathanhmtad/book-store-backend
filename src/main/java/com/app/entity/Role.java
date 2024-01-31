package com.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class Role extends BaseEntity{
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String alias;
    private String description;
}
