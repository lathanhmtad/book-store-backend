package com.app.controller;

import com.app.entity.Brand;
import com.app.payload.BaseResponse;
import com.app.payload.brand.BrandDto;
import com.app.service.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandAPI extends GenericController<Brand, BrandDto>{
    private final BrandService brandService;

    public BrandAPI(BrandService brandService) {
        super(brandService);
        this.brandService = brandService;
    }

    @PostMapping("/import")
    public ResponseEntity<BaseResponse> importBrandData(@RequestParam("file") MultipartFile file) {
        BaseResponse baseResponse = brandService.importBrandData(file);
        return ResponseEntity.ok(baseResponse);
    }
}
