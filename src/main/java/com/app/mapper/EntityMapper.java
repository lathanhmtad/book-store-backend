package com.app.mapper;

import com.app.entity.BaseEntity;
import com.app.payload.BaseDto;

public interface EntityMapper<D extends BaseDto, E extends BaseEntity > {
    E toEntity(D dto);

    D toDto(E entity);

}
