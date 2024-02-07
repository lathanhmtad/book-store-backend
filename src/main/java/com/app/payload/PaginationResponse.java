package com.app.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PaginationResponse<T> {
    private List<T> data;
    private Integer pageNumber;
    private Integer pageSize;
    private String sortField;
    private String sortDir;
    private Integer totalElements;
    private Integer totalPages;
    private Boolean last;
}
