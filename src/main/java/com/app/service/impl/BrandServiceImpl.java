package com.app.service.impl;

import com.app.constant.AppConstant;
import com.app.constant.CloudinaryConstant;
import com.app.entity.Brand;
import com.app.entity.Category;
import com.app.payload.brand.BrandDto;
import com.app.payload.brand.BrandRequest;
import com.app.repository.BrandRepo;
import com.app.repository.CategoryRepo;
import com.app.service.BrandService;
import com.app.util.CloudinaryUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

@Service
public class BrandServiceImpl extends AbstractBaseServiceImpl<Brand, BrandDto> implements BrandService {
    private final BrandRepo brandRepo;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;
    private final CloudinaryUtil cloudinaryUtil;

    public BrandServiceImpl(
                            BrandRepo brandRepo,
                            CloudinaryUtil cloudinaryUtil,
                            ModelMapper modelMapper,
                            CategoryRepo categoryRepo
    ) {
        super(brandRepo, BrandDto.class, Brand.class);
        this.brandRepo = brandRepo;
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
        this.cloudinaryUtil = cloudinaryUtil;
    }

    @Override
    public Brand transformDtoToEntity(BrandDto element) {
        BrandRequest brandRequest = (BrandRequest)  element;
        Long brandId = element.getId();

        if(brandId == null) {
            // create brand
            Brand brandEntity = modelMapper.map(brandRequest, Brand.class);
            this.mapBrandCategories(brandEntity, brandRequest);
            MultipartFile imageFile = brandRequest.getImageFile();
            brandEntity.setLogo(
                    imageFile == null ? AppConstant.BRAND_DEFAULT_IMAGE
                            : cloudinaryUtil.upload(imageFile, CloudinaryConstant.BRAND_IMG_FOLDER)
            );
            return brandEntity;
        }
        else {
            // update brand
            return null;
        }
    }

    private void mapBrandCategories(Brand brand, BrandRequest brandRequest) {
        if(brandRequest.getCategoryIds() != null) {
            List<Category> list = categoryRepo.findAllById(brandRequest.getCategoryIds());
            brand.setCategories(new HashSet<>(list));
        }
        if(brandRequest.getCategoryNames() != null) {
            List<Category> list = brandRequest.getCategoryNames().stream()
                    .map(categoryRepo::findByName).toList();
            brand.setCategories(new HashSet<>(list));
        }
    }
}
