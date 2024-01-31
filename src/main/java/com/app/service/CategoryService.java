package com.app.service;

import com.app.entity.Category;
import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.payload.category.CategoryDto;
import com.app.payload.category.CategoryRequest;
import com.app.payload.category.CategoryTreeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    List<CategoryTreeResponse> getCategoriesTree();
    PaginationResponse<CategoryDto> getCategories(Integer pageNumber, Integer pageSize);
    BaseResponse createNewCategory(CategoryRequest request);
    BaseResponse importCategoryData(MultipartFile file);
}
