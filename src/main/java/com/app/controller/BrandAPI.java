package com.app.controller;

import com.app.constant.AppConstant;
import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.payload.brand.BrandDto;
import com.app.service.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/brands")
public class BrandAPI {
    private BrandService brandService;
    @GetMapping
    public ResponseEntity<PaginationResponse<BrandDto>> getBrands(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "limit", defaultValue = AppConstant.USER_DEFAULT_PAGE_SIZE) Integer limit
    ) {
         PaginationResponse<BrandDto> response = brandService.getBrands(page, limit);
         return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    public ResponseEntity<BaseResponse> importBrandData(@RequestParam("file") MultipartFile file) {
        BaseResponse baseResponse = brandService.importBrandData(file);
        return ResponseEntity.ok(baseResponse);
    }
}
