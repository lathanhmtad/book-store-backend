package com.app.payload.category;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CategoryRequest extends CategoryDto {
    @Nullable
    private MultipartFile imageFile;
    private Long parentId;
    private String parentName;
}
