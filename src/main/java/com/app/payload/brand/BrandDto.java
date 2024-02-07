package com.app.payload.brand;

import com.app.payload.BaseDto;
import com.app.payload.category.CategoryDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BrandDto extends BaseDto {
    private Long id;
    private String name;
    private List<CategoryDto> categories;
}
