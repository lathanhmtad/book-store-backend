package com.app.config;

import com.app.entity.Brand;
import com.app.entity.Category;
import com.app.entity.User;
import com.app.payload.brand.BrandDto;
import com.app.payload.brand.BrandRequest;
import com.app.payload.category.CategoryRequest;
import com.app.payload.user.UserRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        // setup map user request to user entity
        TypeMap<UserRequest, User> userTypeMap = modelMapper.createTypeMap(UserRequest.class, User.class);
        userTypeMap.addMappings(
                mapper -> {
                    mapper.skip(User::setPhoto);
                    mapper.skip(User::setRoles);
                    mapper.skip(User::setPassword);
                }
        );

        // setup map category request to category entity
        TypeMap<CategoryRequest, Category> categoryTypeMap = modelMapper.createTypeMap(CategoryRequest.class, Category.class);
        categoryTypeMap.addMappings(
                mapper -> {
                    mapper.skip(Category::setImage);
                    mapper.skip(Category::setParent);
                }
        );

        // setup map brand entity to brand dto
        TypeMap<Brand, BrandDto> brandTypeMap = modelMapper.createTypeMap(Brand.class, BrandDto.class);
        brandTypeMap.addMappings(
                mapper -> mapper.map(Brand::getCategoryNames, BrandDto::setCategories)
        );

        // setup map brand request to brand entity
        TypeMap<BrandRequest, Brand> brandRequestToBrandTypeMap = modelMapper.createTypeMap(BrandRequest.class, Brand.class);
        brandRequestToBrandTypeMap.addMappings(
                mapper -> {
                    mapper.skip(Brand::setCategories);
                    mapper.skip(Brand::setLogo);
                }
        );

        return modelMapper;
    }
}