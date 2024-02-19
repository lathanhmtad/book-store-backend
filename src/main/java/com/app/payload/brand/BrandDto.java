package com.app.payload.brand;

import com.app.payload.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BrandDto extends BaseDto {
    private Long id;
    private String name;
    private String logo;
    private List<String> categories;
}
