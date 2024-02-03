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
import com.app.payload.user.UserCreationDto;
import com.app.repository.RoleRepo;
import com.app.repository.UserRepo;
import com.app.security.MyUserDetails;
import com.app.service.CloudinaryService;
import com.app.service.UserService;
import com.app.util.ExcelUtil;
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
import java.util.Optional;
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
    public UserDto getUserById(Long userId) {
        try {
            User userEntity = userRepo.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("User", "id", String.valueOf(userId)));
            return modelMapper.map(userEntity, UserDto.class);
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.PROCESSING);
        }
    }

    @Override
    public UserDto retrieveCurrentUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = userDetails.getId();
        return getUserById(currentUserId);
    }

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
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse saveUser(UserCreationDto userRequest) {
        if(!this.isEmailUnique(userRequest.getId(), userRequest.getEmail()))
            throw new BookStoreApiException("Email already exists, please use another email!", HttpStatus.CONFLICT);

        boolean isUpdatingUser = userRequest.getId() != null;
        try {
            if(isUpdatingUser) {
                User existingUser = userRepo.findById(userRequest.getId()).get();
                this.mapUserRequestToUserEntity(userRequest, existingUser);
                userRepo.save(existingUser);
                return new BaseResponse(200, "Update user success!");
            }
            User userEntity = new User();
            this.mapUserRequestToUserEntity(userRequest, userEntity);
            userRepo.save(userEntity);
            return new BaseResponse(200, "Create user success!");
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BaseResponse importUserData(MultipartFile importFile) {
        try {
            boolean isValid = ExcelUtil.isValidExcelFile(importFile);
            if(isValid) {
                Workbook workbook = ExcelUtil.getWorkbookStream(importFile);
                List<UserCreationDto> userDtos = ExcelUtil.getImportData(workbook, ExcelImportConfig.userImportConfig);

                userDtos.forEach((userDto) -> {
                    this.saveUser(userDto);
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

    private boolean isEmailUnique(Long userId, String email) {
        Optional<User> userByEmail = userRepo.findByEmail(email);

        if(userByEmail.isEmpty()) return true;

        boolean isCreatingNew = (userId == null);

        if(isCreatingNew) {
            if(userByEmail.isPresent()) return false;
        } else {
            if(userByEmail.get().getId() != userId) {
                return false;
            }
        }
        return true;
    }

    private void mapUserRequestToUserEntity(UserCreationDto source, User destination) throws Exception {
        modelMapper.map(source, destination);
        if(source.getRoleIds() != null) {
            destination.setRoles(roleRepo.findAllById(source.getRoleIds()));
        }
        if(source.getRoleNames() != null) {
            destination.setRoles(roleRepo.findAllByName(source.getRoleNames()));
        }

        MultipartFile imageFile = source.getImageFile();
        destination.setPhoto(
                imageFile == null ?
                        ((destination.getPhoto() == null ? AppConstant.USER_DEFAULT_IMAGE : destination.getPhoto()))
                        :
                        cloudinaryImageService.upload(imageFile, CloudinaryUploadFolderConstant.USER_IMG_FOLDER)
        );

        if(source.getId() != null && source.getPassword().isEmpty()) {

        }
        else {
            destination.setPassword(encodePassword(source.getPassword()));
        }
    }
}
