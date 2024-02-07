package com.app.controller;

import com.app.constant.AppConstant;
import com.app.entity.BaseEntity;
import com.app.payload.BaseDto;
import com.app.payload.PaginationResponse;
import com.app.service.AbstractBaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public class GenericController<TModel extends BaseEntity, TDto extends BaseDto> {
    private final AbstractBaseService<TModel, TDto> abstractBaseService;

    public GenericController(AbstractBaseService<TModel, TDto> abstractBaseService) {
        this.abstractBaseService = abstractBaseService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<TDto>> findAll(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "limit", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) Integer limit,
            @RequestParam(value = "sortField", defaultValue = AppConstant.DEFAULT_SORT_FIELD, required = false) String sortField,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_DIR, required = false) String sortDir
    ) {
        PaginationResponse<TDto> response = abstractBaseService.findAll(page, limit, sortField, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TDto> findById(@PathVariable("id") Long userId) {
        TDto response = abstractBaseService.findById(userId);
        return ResponseEntity.ok(response);
    }
}
