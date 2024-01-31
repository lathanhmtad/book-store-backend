package com.app.service.impl;

import com.app.config.ExcelImportConfig;
import com.app.constant.AppConstant;
import com.app.constant.CloudinaryUploadFolderConstant;
import com.app.entity.User;
import com.app.exception.BookStoreApiException;
import com.app.exception.ResourceNotFoundException;
import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;
import com.app.repository.RoleRepo;
import com.app.repository.UserRepo;
import com.app.security.MyUserDetails;
import com.app.service.CloudinaryService;
import com.app.service.UserService;
import com.app.util.ExcelUtil;
import com.app.util.FileFactory;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private RoleRepo roleRepo;
    private CloudinaryService cloudinaryImageService;
    private ModelMapper modelMapper;
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    @Override
    public PaginationResponse<UserDto> getUsers(Integer pageNumber, Integer limit) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, limit, sort);
        Page<User> page = userRepo.findAll(pageable) ;

        List<User> users = page.getContent();

        PaginationResponse<UserDto> response = PaginationResponse.<UserDto>builder()
                .data(users.stream().map(item -> modelMapper.map(item, UserDto.class)).collect(Collectors.toList()))
                .totalPages(page.getTotalPages())
                .totalElements((int) page.getTotalElements())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .last(page.isLast())
                .build();
        return response;
    }

    @Override
    public UserDto getUserById(Long userId) {
        User userEntity = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(userId)));
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto retrieveCurrentUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = userDetails.getId();
        return getUserById(currentUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createNewUser(UserRequest userRequest) {
        try {
            User userEntity = modelMapper.map(userRequest, User.class);
            if(userRequest.getRoleIds() != null) {
                userEntity.setRoles(roleRepo.findAllById(userRequest.getRoleIds()));
            }
            if(userRequest.getRoleNames() != null) {
                userEntity.setRoles(roleRepo.findAllByName(userRequest.getRoleNames()));
            }
            MultipartFile multipartFile = userRequest.getImage();
            if(multipartFile != null) {
                String imgUrl = cloudinaryImageService.upload(multipartFile, CloudinaryUploadFolderConstant.USER_IMG_FOLDER);
                userEntity.setPhoto(imgUrl);
            }
            else {
                userEntity.setPhoto(AppConstant.USER_DEFAULT_IMAGE);
            }
            userEntity.setPassword(encodePassword(userEntity.getPassword()));
            userRepo.save(userEntity);

            return new BaseResponse(200, "Create new user success!");
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BaseResponse importUserData(MultipartFile importFile) {
        try {
            boolean isValid = ExcelUtil.isValidExcelFile(importFile);
            if(isValid) {
                Workbook workbook = FileFactory.getWorkbookStream(importFile);
                List<UserRequest> userDtos = ExcelUtil.getImportData(workbook, ExcelImportConfig.userImportConfig);

                userDtos.forEach((userDto) -> {
                    this.createNewUser(userDto);
                });

                return new BaseResponse(200, "Create new user success!");
            }
            else {
                throw new Exception("File excel not valid!");
            }
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deleteUser(Long id) {
        try {
             User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(id)));
             cloudinaryImageService.delete(user.getPhoto(), CloudinaryUploadFolderConstant.USER_IMG_FOLDER);
             userRepo.deleteById(id);
                return new BaseResponse(200, "Delete user success!");
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
