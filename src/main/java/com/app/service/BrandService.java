package com.app.service;

import com.app.entity.Brand;
import com.app.payload.BaseResponse;
import com.app.payload.brand.BrandDto;
import com.app.payload.brand.BrandRequest;
import org.springframework.web.multipart.MultipartFile;

public interface BrandService extends AbstractBaseService<Brand, BrandDto> {
    BaseResponse createNewBrand(BrandRequest brandRequest);
    BaseResponse importBrandData(MultipartFile file);
}
