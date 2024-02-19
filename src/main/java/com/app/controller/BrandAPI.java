package com.app.controller;

import com.app.entity.Brand;
import com.app.payload.brand.BrandDto;
import com.app.payload.brand.BrandRequest;
import com.app.service.BrandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandAPI extends GenericController<Brand, BrandDto, BrandRequest>{
    public BrandAPI(BrandService brandService) {
        super(brandService);
    }
}
