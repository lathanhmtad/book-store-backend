package com.app.service.impl;

import com.app.entity.BaseEntity;
import com.app.exception.BookStoreApiException;
import com.app.exception.ResourceNotFoundException;
import com.app.payload.BaseDto;
import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.repository.AbstractBaseRepo;
import com.app.service.AbstractBaseService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class AbstractBaseServiceImpl<TModel extends BaseEntity, TDto extends BaseDto> implements AbstractBaseService<TModel, TDto> {
    private final AbstractBaseRepo<TModel> abstractBaseRepo;
    private final ModelMapper modelMapper;
    private final Class<TDto> dtoClass;
    private final Class<TModel> entityClass;
    public AbstractBaseServiceImpl(AbstractBaseRepo<TModel> abstractBaseRepo,
                                   ModelMapper modelMapper,
                                   Class<TDto> dtoClass,
                                   Class<TModel> entityClass) {
        this.abstractBaseRepo = abstractBaseRepo;
        this.modelMapper = modelMapper;
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
    }

    @Override
    public PaginationResponse<TDto> findAll(Integer pageNumber, Integer size, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, size, sort);
        Page<TModel> page = abstractBaseRepo.findAll(pageable) ;

        List<TModel> tModels = page.getContent();

       return PaginationResponse.<TDto>builder()
                .data(tModels.stream().map(item -> modelMapper.map(item, dtoClass)).collect(Collectors.toList()))
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
               .sortField(sortField)
               .sortDir(sortDir)
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    public TDto findById(Long id) {
        TModel tModel = abstractBaseRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(entityClass.getSimpleName(), "id", String.valueOf(id)));
        return modelMapper.map(tModel, dtoClass);
    }

    @Override
    public BaseResponse saveOrUpdate(TDto element) {
        try {
            TModel entity = this.transformDtoToEntity(element);
//            this.abstractBaseRepo.save(entity);

            return new BaseResponse(200, "modification " + entityClass.getSimpleName() + " operation success!");
        } catch (Exception e) {
          throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public TModel transformDtoToEntity(TDto element) throws Exception {
        return modelMapper.map(element, entityClass);
    }

    @Override
    public TDto transformEntityToDto(TModel element) {
        return modelMapper.map(element, dtoClass);
    }
}
