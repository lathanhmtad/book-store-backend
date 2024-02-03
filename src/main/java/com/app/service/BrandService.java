package com.app.service;

import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.payload.brand.BrandDto;
import com.app.payload.brand.BrandRequest;
import org.springframework.web.multipart.MultipartFile;

public interface BrandService {
    PaginationResponse<BrandDto> getBrands(Integer page, Integer limit);
    BaseResponse createNewBrand(BrandRequest brandRequest);
    BaseResponse importBrandData(MultipartFile file);
}
