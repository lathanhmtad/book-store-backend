package com.app.service;

import com.app.entity.BaseEntity;
import com.app.exception.ResourceExistsException;
import com.app.payload.BaseDto;
import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AbstractBaseService<TModel extends BaseEntity, TDto extends BaseDto>  {
    PaginationResponse<TDto> findAll(Integer pageNumber, Integer size, String sortField, String sortDir);
    TDto findById(Long id);
    BaseResponse saveOrUpdate(TDto element);
    BaseResponse importExcelData(MultipartFile excelFile);
    BaseResponse deleteById(Long id);
    boolean isExists(TDto element) throws ResourceExistsException;
    TModel transformDtoToEntity(TDto element) throws Exception;
    TDto transformEntityToDto(TModel element);
}
