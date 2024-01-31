package com.app.payload.category;

import com.app.payload.AuditableDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto extends AuditableDto<Long> {
    private Long id;
    private String name;
    private String image;
    private Boolean enabled;
}
