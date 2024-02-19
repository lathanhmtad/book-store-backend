package com.app.payload.category;

import com.app.payload.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto extends BaseDto {
    private Long id;
    private String name;
    private String image;
    private Boolean enabled;
    private Boolean hasChildren;
}
