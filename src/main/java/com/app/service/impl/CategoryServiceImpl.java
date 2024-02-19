package com.app.service.impl;

import com.app.constant.AppConstant;
import com.app.constant.CloudinaryConstant;
import com.app.entity.Category;
import com.app.exception.BookStoreApiException;
import com.app.exception.ResourceNotFoundException;
import com.app.payload.category.CategoryDetailsDto;
import com.app.payload.category.CategoryDto;
import com.app.payload.category.CategoryRequest;
import com.app.payload.category.CategoryTreeDto;
import com.app.repository.CategoryRepo;
import com.app.service.CategoryService;
import com.app.util.CloudinaryUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl extends AbstractBaseServiceImpl<Category, CategoryDto> implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;
    private final CloudinaryUtil cloudinaryUtil;

    public CategoryServiceImpl(CategoryRepo categoryRepo,
                               ModelMapper modelMapper,
                               CloudinaryUtil cloudinaryUtil) {
        super(categoryRepo, CategoryDto.class, Category.class);
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
        this.cloudinaryUtil = cloudinaryUtil;
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
    public CategoryTreeDto getCategoryTree(Long categoryId) {
        Category categoryInDb = categoryRepo.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(categoryId)));
        CategoryTreeDto categoryTreeDto = new CategoryTreeDto(categoryInDb.getName(), categoryInDb.getId());
        this.addChildren(categoryTreeDto.getChildren(), categoryInDb, 1);
        return categoryTreeDto;
    }

    @Override
    public CategoryDto findById(Long categoryId) {
        Category categoryInDb = categoryRepo.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(categoryId)));
        CategoryDto categoryDto = this.transformEntityToDto(categoryInDb);
        CategoryDetailsDto categoryDetailsDto = modelMapper.map(categoryDto, CategoryDetailsDto.class);
        this.addChildren(categoryDetailsDto.getChildren(), categoryInDb, 1);
        return categoryDetailsDto;
    }

    @Override
    public Category transformDtoToEntity(CategoryDto categoryDto) {
        CategoryRequest categoryRequest = (CategoryRequest) categoryDto;
        Long categoryId = categoryRequest.getId();

        if(categoryId == null) {
            // create category
            Category categoryEntity = modelMapper.map(categoryRequest, Category.class);
            this.mapParentCategory(categoryEntity, categoryRequest);

            MultipartFile imgFile = categoryRequest.getImageFile();
            categoryEntity.setImage(
                    imgFile == null ? AppConstant.CATEGORY_DEFAULT_IMAGE :
                            cloudinaryUtil.upload(imgFile, CloudinaryConstant.CATEGORY_IMG_FOLDER)
            );
            return categoryEntity;
        }
        else {
            // update category
            Category categoryInDb = categoryRepo.findById(categoryId).
                    orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(categoryId)));
            modelMapper.map(categoryRequest, categoryInDb);
            this.mapParentCategory(categoryInDb, categoryRequest);

            MultipartFile imgFile = categoryRequest.getImageFile();
            if(imgFile != null) {
                if(!categoryInDb.getImage().equals(AppConstant.CATEGORY_DEFAULT_IMAGE)) {
                    cloudinaryUtil.delete(categoryInDb.getImage());
                }
                categoryInDb.setImage(cloudinaryUtil.upload(imgFile, CloudinaryConstant.USER_IMG_FOLDER));
            }
            return categoryInDb;
        }
    }

    @Override
    public CategoryDto transformEntityToDto(Category element) {
        CategoryDto categoryDto = modelMapper.map(element, CategoryDto.class);
        categoryDto.setHasChildren(element.getChildren().size() > 0);
        return categoryDto;
    }

    private void mapParentCategory(Category category, CategoryRequest categoryRequest) {
        if(categoryRequest.getParentId() != null && categoryRequest.getParentId() != -1) {
             category.setParent(categoryRepo.findById(categoryRequest.getParentId()).orElseThrow(
                    () -> new ResourceNotFoundException("Category", "id", String.valueOf(categoryRequest.getId()))
            ));
        }
        else if(categoryRequest.getParentName() != null) {
            category.setParent(categoryRepo.findByName(categoryRequest.getParentName()));
        }
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
}
