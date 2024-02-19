package com.app.service.impl;

import com.app.config.ExcelImportConfig;
import com.app.entity.BaseEntity;
import com.app.exception.BookStoreApiException;
import com.app.exception.ResourceExistsException;
import com.app.exception.ResourceNotFoundException;
import com.app.payload.BaseDto;
import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.repository.AbstractBaseRepo;
import com.app.service.AbstractBaseService;
import com.app.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class AbstractBaseServiceImpl<TModel extends BaseEntity, TDto extends BaseDto> implements AbstractBaseService<TModel, TDto> {
    private final AbstractBaseRepo<TModel> abstractBaseRepo;
    private final Class<TDto> dtoClass;
    private final Class<TModel> entityClass;
    @Autowired
    private ModelMapper modelMapper;
    public AbstractBaseServiceImpl(AbstractBaseRepo<TModel> abstractBaseRepo,
                                   Class<TDto> dtoClass,
                                   Class<TModel> entityClass) {
        this.abstractBaseRepo = abstractBaseRepo;
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
                .data(tModels.stream().map(this::transformEntityToDto).collect(Collectors.toList()))
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
        return this.transformEntityToDto(tModel);
    }

    @Override
    public BaseResponse saveOrUpdate(TDto element) {
        try {
            if(isExists(element)) return null;
            TModel entity = this.transformDtoToEntity(element);
            String operationText = entity.getId() != null ? "Update" : "Create";
            this.abstractBaseRepo.save(entity);
            return new BaseResponse(200, String.format("%s %s success", operationText, entityClass.getSimpleName().toLowerCase()));
        }
        catch (ResourceExistsException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BaseResponse importExcelData(MultipartFile excelFile) {
        try {
            boolean isValid = ExcelUtil.isValidExcelFile(excelFile);
            if(isValid) {
                Workbook workbook = ExcelUtil.getWorkbookStream(excelFile);
                List<TDto> tDtos = ExcelUtil.getImportData(workbook, ExcelImportConfig.getImportConfig(entityClass.getSimpleName()));

                tDtos.forEach(this::saveOrUpdate);

                return new BaseResponse(200, "Imported excel " + entityClass.getSimpleName().toLowerCase() + " data successfully");
            }
            throw new Exception("File excel not valid!");
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BaseResponse deleteById(Long id) {
        try {
            TModel model = abstractBaseRepo.
                    findById(id).orElseThrow(() -> new ResourceNotFoundException(entityClass.getSimpleName(), "id", String.valueOf(id)));
            abstractBaseRepo.delete(model);
            return new BaseResponse(200, "Delete " + entityClass.getSimpleName().toLowerCase() + " success");
        }
        catch (ResourceNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean isExists(TDto element) throws ResourceExistsException {
        return false;
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
