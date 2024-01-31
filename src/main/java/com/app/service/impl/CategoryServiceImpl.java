package com.app.service.impl;

import com.app.config.ExcelImportConfig;
import com.app.entity.Category;
import com.app.exception.BookStoreApiException;
import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.payload.category.CategoryDto;
import com.app.payload.category.CategoryRequest;
import com.app.payload.category.CategoryTreeResponse;
import com.app.repository.CategoryRepo;
import com.app.service.CategoryService;
import com.app.util.ExcelUtil;
import com.app.util.FileFactory;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepo categoryRepo;
    private ModelMapper modelMapper;

    @Override
    public List<CategoryTreeResponse> getCategoriesTree() {
        try {
            List<Category> categoriesInDb = categoryRepo.findAll();

            List<CategoryTreeResponse> response = new ArrayList<>();

            for (int i = 0; i < categoriesInDb.size(); i++) {
                if(categoriesInDb.get(i).getParent() == null) {
                    response.add(CategoryTreeResponse.builder()
                            .title(categoriesInDb.get(i).getName())
                            .value(categoriesInDb.get(i).getId())
                            .children(new ArrayList<>())
                            .build());
                    Set<Category> children = categoriesInDb.get(i).getChildren();
                    for(Category subCategory : children) {

                        response.get(i).getChildren().add(CategoryTreeResponse.builder()
                                .title(subCategory.getName())
                                .value(subCategory.getId())
                                .children(new ArrayList<>())
                                .build());
                        listChildren(response.get(i).getChildren(), subCategory, 1);
                    }
                }
            }
            return response;
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public PaginationResponse<CategoryDto> getCategories(Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Category> page = categoryRepo.findAll(pageable);
        List<Category> categories = page.getContent();

        return PaginationResponse.<CategoryDto>builder()
                .data(categories.stream().map(category -> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList()))
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .totalPages(page.getTotalPages())
                .totalElements((int)page.getTotalElements())
                .last(page.isLast())
                .build();
    }

    @Override
    public BaseResponse createNewCategory(CategoryRequest request) {
        try {
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            Category parent = null;
            if(request.getParentId() != null) {
                parent = categoryRepo.findById(request.getParentId()).get();
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
                Workbook workbook = FileFactory.getWorkbookStream(file);
                List<CategoryRequest> categoryDtos = ExcelUtil.getImportData(workbook, ExcelImportConfig.categoryImportConfig);

                categoryDtos.forEach(item -> {
                    this.createNewCategory(item);
                });

                return new BaseResponse(200, "Create success");
            }
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private void listChildren(List<CategoryTreeResponse> response, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for(Category subCategory : children) {
            response.get(subLevel -1).getChildren().add(CategoryTreeResponse.builder()
                    .title(subCategory.getName())
                    .value(subCategory.getId())
                    .children(new ArrayList<>())
                    .build());
            listChildren(response.get(subLevel - 1).getChildren(), subCategory, newSubLevel);
        }
    }
}
