package com.app.payload.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryTreeDto {
    private String title;
    private Long value;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CategoryTreeDto> children = new ArrayList<>();

    public CategoryTreeDto(String title, Long value) {
        this.title = title;
        this.value = value;
    }
}
