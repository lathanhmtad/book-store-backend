package com.app.payload.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryDetailsDto extends CategoryDto {
    private List<CategoryTreeDto> children = new ArrayList<>();


}
