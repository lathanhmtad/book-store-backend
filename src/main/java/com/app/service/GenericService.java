package com.app.service;

import com.app.entity.BaseEntity;
import com.app.payload.PaginationResponse;

public interface GenericService<TModel extends BaseEntity, TDto> {
    PaginationResponse<TDto> getAll(Integer page, Integer size);
}
