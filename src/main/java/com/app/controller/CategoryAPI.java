package com.app.controller;

import com.app.entity.Category;
import com.app.payload.category.CategoryDto;
import com.app.payload.category.CategoryRequest;
import com.app.payload.category.CategoryTreeDto;
import com.app.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryAPI extends GenericController<Category, CategoryDto, CategoryRequest> {
    private final CategoryService categoryService;

    public CategoryAPI(CategoryService categoryService) {
        super(categoryService);
        this.categoryService = categoryService;
    }

    @GetMapping("/tree")
    public ResponseEntity<List<CategoryTreeDto>> getCategories() {
        List<CategoryTreeDto> response = categoryService.getCategoriesTree();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tree/{id}")
    public ResponseEntity<CategoryTreeDto> getCategory(@PathVariable("id") Long categoryId) {
        CategoryTreeDto categoryTreeDto = categoryService.getCategoryTree(categoryId);
        return ResponseEntity.ok(categoryTreeDto);
    }
}
