package com.app.repository;

import com.app.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    Category findByName(String name);
}
