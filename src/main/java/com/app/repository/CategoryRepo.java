package com.app.repository;

import com.app.entity.Category;

public interface CategoryRepo extends AbstractBaseRepo<Category> {

    Category findByName(String name);
}
