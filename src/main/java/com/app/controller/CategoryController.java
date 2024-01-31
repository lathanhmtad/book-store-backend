package com.app.controller;

import com.app.constant.AppConstant;
import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.payload.category.CategoryDto;
import com.app.payload.category.CategoryRequest;
import com.app.payload.category.CategoryTreeResponse;
import com.app.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private CategoryService categoryService;
    @GetMapping
    public ResponseEntity<PaginationResponse<CategoryDto>> getCategories(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "limit", defaultValue = AppConstant.USER_DEFAULT_PAGE_SIZE) Integer limit
    ) {
        PaginationResponse<CategoryDto> response = categoryService.getCategories(page, limit);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/tree")
    public ResponseEntity<List<CategoryTreeResponse>> getCategories() {
        List<CategoryTreeResponse> response = categoryService.getCategoriesTree();
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<BaseResponse> createCategory(@ModelAttribute CategoryRequest request) {
        BaseResponse response = categoryService.createNewCategory(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    public ResponseEntity<BaseResponse> importCategoryData(@RequestParam("file") MultipartFile file) {
        BaseResponse response = categoryService.importCategoryData(file);
        return ResponseEntity.ok(response);
    }
}
