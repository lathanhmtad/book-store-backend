package com.app.payload.brand;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BrandDto {
    private Long id;
    private String name;
    private List<String> categories;
}
