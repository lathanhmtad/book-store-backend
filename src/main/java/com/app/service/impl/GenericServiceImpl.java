package com.app.service.impl;

import com.app.entity.BaseEntity;
import com.app.payload.PaginationResponse;
import com.app.repository.GenericRepo;
import com.app.service.GenericService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GenericServiceImpl<TModel extends BaseEntity, TDto> implements GenericService<TModel, TDto> {
    private final GenericRepo<TModel> genericRepo;
    private ModelMapper modelMapper;
    @Override
    public PaginationResponse getAll(Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<TModel> page = genericRepo.findAll(pageable);

        List<TModel> tModels = page.getContent();

//        return PaginationResponse.<TDto>builder()
//                .data(tModels.stream().map(item -> modelMapper.map(item, TDto.c)))
//                .build();

        return null;
    }
}
