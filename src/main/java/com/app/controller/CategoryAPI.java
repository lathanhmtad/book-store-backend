package com.app.controller;

import com.app.entity.Category;
import com.app.payload.BaseResponse;
import com.app.payload.category.CategoryDto;
import com.app.payload.category.CategoryRequest;
import com.app.payload.category.CategoryTreeDto;
import com.app.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryAPI extends GenericController<Category, CategoryDto> {
    private CategoryService categoryService;

    public CategoryAPI(CategoryService categoryService) {
        super(categoryService);
        this.categoryService = categoryService;
    }

    @GetMapping("/tree")
    public ResponseEntity<List<CategoryTreeDto>> getCategories() {
        List<CategoryTreeDto> response = categoryService.getCategoriesTree();
        return ResponseEntity.ok(response);
    }
//    @PostMapping
//    public ResponseEntity<BaseResponse> createCategory(@ModelAttribute CategoryRequest request) {
//        BaseResponse response = categoryService.createNewCategory(request);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/import")
    public ResponseEntity<BaseResponse> importCategoryData(@RequestParam("file") MultipartFile file) {
        BaseResponse response = categoryService.importCategoryData(file);
        return ResponseEntity.ok(response);
    }
}
