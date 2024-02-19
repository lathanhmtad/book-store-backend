package com.app.payload.brand;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class BrandRequest extends BrandDto {
    private MultipartFile imageFile;
    private List<String> categoryNames;
    private Set<Long> categoryIds;
}
