package com.app.service.impl;

import com.app.config.ExcelImportConfig;
import com.app.entity.Category;
import com.app.exception.BookStoreApiException;
import com.app.payload.BaseResponse;
import com.app.payload.category.CategoryDto;
import com.app.payload.category.CategoryRequest;
import com.app.payload.category.CategoryTreeDto;
import com.app.repository.CategoryRepo;
import com.app.service.CategoryService;
import com.app.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl extends AbstractBaseServiceImpl<Category, CategoryDto> implements CategoryService {
    private CategoryRepo categoryRepo;
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepo categoryRepo,
                               ModelMapper modelMapper) {
        super(categoryRepo, modelMapper, CategoryDto.class, Category.class);
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoryTreeDto> getCategoriesTree() {
        try {
            List<Category> categoriesInDb = categoryRepo.findAll();

            List<CategoryTreeDto> response = new ArrayList<>();

            for(Category category : categoriesInDb) {
                if(category.getParent() == null) {
                    response.add(new CategoryTreeDto(
                            category.getName(),
                            category.getId()
                    ));
                    this.addChildren(response.get(response.size() - 1).getChildren(), category, 1);
                }
            }

            response.add(0, new CategoryTreeDto("No parent", -1L));
            return response;
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public CategoryTreeDto getCategoryTree() {
        return null;
    }

    @Override
    public BaseResponse createNewCategory(CategoryRequest request) {
        try {
            Category parent = null;
            if(request.getParentId() != null && request.getParentId() != -1) {
                parent = categoryRepo.findById(request.getParentId()).orElseThrow(
                        () -> new BookStoreApiException("Category parent not found for id " + request.getParentId(), HttpStatus.BAD_REQUEST)
                );
            }
            else if(request.getParentName() != null) {
                parent = categoryRepo.findByName(request.getParentName());
            }
            Category entity = modelMapper.map(request, Category.class);
            entity.setParent(parent);
            categoryRepo.save(entity);
            return new BaseResponse(200, "Create success");
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BaseResponse importCategoryData(MultipartFile file) {
        try {
            boolean isValid = ExcelUtil.isValidExcelFile(file);
            if(isValid) {
                Workbook workbook = ExcelUtil.getWorkbookStream(file);
                List<CategoryRequest> categoryDtos = ExcelUtil.getImportData(workbook, ExcelImportConfig.categoryImportConfig);

                categoryDtos.forEach(this::createNewCategory);

                return new BaseResponse(200, "Create success");
            }
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public BaseResponse updateCategory(CategoryRequest categoryRequest) {
        return null;
    }

    private void addChildren(List<CategoryTreeDto> list, Category category, int depth) {
        for(Category child : category.getChildren()) {
            CategoryTreeDto childResponse = new CategoryTreeDto(
                    child.getName(),
                    child.getId()
            );
            list.add(childResponse);
            addChildren(childResponse.getChildren(), child, depth + 1);
        }
    }

    @Override
    public Category transformDtoToEntity(CategoryDto element) {
        return null;
    }

    @Override
    public CategoryDto transformEntityToDto(Category element) {
        return null;
    }
}
