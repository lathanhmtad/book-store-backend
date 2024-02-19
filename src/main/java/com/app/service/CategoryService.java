package com.app.service;

import com.app.entity.Category;
import com.app.payload.category.CategoryDto;
import com.app.payload.category.CategoryTreeDto;

import java.util.List;

public interface CategoryService extends AbstractBaseService<Category, CategoryDto> {
    List<CategoryTreeDto> getCategoriesTree();
    CategoryTreeDto getCategoryTree(Long categoryId);
}
