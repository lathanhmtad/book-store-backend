package com.app.service.impl;

import com.app.config.ExcelImportConfig;
import com.app.constant.CloudinaryUploadFolderConstant;
import com.app.entity.Brand;
import com.app.entity.Category;
import com.app.exception.BookStoreApiException;
import com.app.payload.BaseResponse;
import com.app.payload.brand.BrandDto;
import com.app.payload.brand.BrandRequest;
import com.app.repository.BrandRepo;
import com.app.repository.CategoryRepo;
import com.app.service.BrandService;
import com.app.service.CloudinaryService;
import com.app.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl extends AbstractBaseServiceImpl<Brand, BrandDto> implements BrandService {
    private final BrandRepo brandRepo;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    public BrandServiceImpl(ModelMapper modelMapper,
                            BrandRepo brandRepo,
                            CloudinaryService cloudinaryService,
                            CategoryRepo categoryRepo
    ) {
        super(brandRepo, modelMapper, BrandDto.class, Brand.class);
        this.brandRepo = brandRepo;
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createNewBrand(BrandRequest brandRequest) {
        try {
            Set<Category> categories = brandRequest.getCategoryNames().stream()
                    .map(name -> categoryRepo.findByName(name)).collect(Collectors.toSet());

            Brand brand = modelMapper.map(brandRequest, Brand.class);
            brand.setCategories(categories);

            if(brandRequest.getImageFile() != null) {
                String urlImg = cloudinaryService.upload(brandRequest.getImageFile(), CloudinaryUploadFolderConstant.BRAND_IMG_FOLDER);
                brand.setLogo(urlImg);
            }
            brandRepo.save(brand);

            return new BaseResponse(200, "Create brand success!");

        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BaseResponse importBrandData(MultipartFile file) {
        try {
            boolean isValid = ExcelUtil.isValidExcelFile(file);
            if(isValid) {
                Workbook workbook = ExcelUtil.getWorkbookStream(file);
                List<BrandRequest> brandDtos = ExcelUtil.getImportData(workbook, ExcelImportConfig.brandImportConfig);

                brandDtos.forEach(this::createNewBrand);

                return new BaseResponse(200, "Create success");
            }
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public Brand transformDtoToEntity(BrandDto element) throws Exception {
        return super.transformDtoToEntity(element);
    }

    @Override
    public BrandDto transformEntityToDto(Brand element) {
        return super.transformEntityToDto(element);
    }
}
