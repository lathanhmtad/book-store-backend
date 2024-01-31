package com.app.payload.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CategoryTreeResponse {
    private Long value;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CategoryTreeResponse> children;
}
