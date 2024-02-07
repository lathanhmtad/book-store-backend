package com.app.service;

import com.app.entity.Category;
import com.app.payload.BaseResponse;
import com.app.payload.category.CategoryDto;
import com.app.payload.category.CategoryRequest;
import com.app.payload.category.CategoryTreeDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService extends AbstractBaseService<Category, CategoryDto> {
    List<CategoryTreeDto> getCategoriesTree();
    CategoryTreeDto getCategoryTree();
    BaseResponse createNewCategory(CategoryRequest request);
    BaseResponse importCategoryData(MultipartFile file);
    BaseResponse updateCategory(CategoryRequest categoryRequest);
}
