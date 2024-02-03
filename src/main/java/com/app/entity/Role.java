package com.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@SQLDelete(sql = "UPDATE role SET deleted = true WHERE id=?")
@Where(clause = "deleted=false or deleted is null")
@Entity
public class Role extends BaseEntity{
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String alias;
    private String description;
}
