package com.app.controller;

import com.app.constant.AppConstant;
import com.app.entity.BaseEntity;
import com.app.payload.BaseDto;
import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.service.AbstractBaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public class GenericController<TModel extends BaseEntity, TDto extends BaseDto, TDtoRequest extends TDto> {
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
    public ResponseEntity<TDto> findById(@PathVariable("id") Long id) {
        TDto response = abstractBaseService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/excel")
    public ResponseEntity<?> importExcelData(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(
                abstractBaseService.importExcelData(file)
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute TDtoRequest creationDto) {
        return ResponseEntity.ok(
                abstractBaseService.saveOrUpdate(creationDto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> update(@PathVariable("id") Long id, @ModelAttribute TDtoRequest toUpdate) {
        toUpdate.setId(id);
        return ResponseEntity.ok(
                abstractBaseService.saveOrUpdate(toUpdate)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                abstractBaseService.deleteById(id)
        );
    }
}
