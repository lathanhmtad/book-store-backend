package com.app.service.impl;

import com.app.config.ExcelImportConfig;
import com.app.constant.AppConstant;
import com.app.constant.CloudinaryUploadFolderConstant;
import com.app.entity.User;
import com.app.exception.BookStoreApiException;
import com.app.exception.ResourceNotFoundException;
import com.app.payload.BaseResponse;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;
import com.app.repository.RoleRepo;
import com.app.repository.UserRepo;
import com.app.security.MyUserDetails;
import com.app.service.CloudinaryService;
import com.app.service.UserService;
import com.app.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractBaseServiceImpl<User, UserDto> implements UserService {
    private RoleRepo roleRepo;
    private CloudinaryService cloudinaryImageService;
    private ModelMapper modelMapper;
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo,
                           ModelMapper modelMapper,
                           RoleRepo roleRepo,
                           CloudinaryService cloudinaryService,
                           PasswordEncoder passwordEncoder
    ) {
        super(userRepo, modelMapper, UserDto.class, User.class);
        this.modelMapper = modelMapper;
        this.roleRepo = roleRepo;
        this.cloudinaryImageService = cloudinaryService;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public UserDto retrieveCurrentUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = userDetails.getId();
        return this.findById(currentUserId);
    }

    @Override
    public BaseResponse importUserData(MultipartFile importFile) {
//        try {
//            boolean isValid = ExcelUtil.isValidExcelFile(importFile);
//            if(isValid) {
//                Workbook workbook = ExcelUtil.getWorkbookStream(importFile);
//                List<UserRequest> userDtos = ExcelUtil.getImportData(workbook, ExcelImportConfig.userImportConfig);
//
//                userDtos.forEach((userDto) -> {
//                    this.saveUser(userDto);
//                });
//
//                return new BaseResponse(200, "Create new user success!");
//            }
//            else {
//                throw new Exception("File excel not valid!");
//            }
//        } catch (Exception e) {
//            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
        return null;
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

    @Override
    public User transformDtoToEntity(UserDto userDto) throws Exception {
        UserRequest userRequest = (UserRequest) userDto;

        Long userId = userRequest.getId();

        if(userId == null) {
            // create user
            User userEntity = modelMapper.map(userRequest, User.class);
            if(userRequest.getRoleIds() != null) {
                userEntity.setRoles(roleRepo.findAllById(userRequest.getRoleIds()));
            }
            if(userRequest.getRoleNames() != null) {
                userEntity.setRoles(roleRepo.findAllByName(userRequest.getRoleNames()));
            }

            MultipartFile imgFile = userRequest.getImageFile();
            userEntity.setPhoto(
                    imgFile == null ? AppConstant.USER_DEFAULT_IMAGE :
                            cloudinaryImageService.upload(imgFile, CloudinaryUploadFolderConstant.USER_IMG_FOLDER)
            );
            userEntity.setPassword(encodePassword(userRequest.getPassword()));
            return userEntity;
        }
        else {
            // update user
            User userInDb = userRepo.findById(userId).get();
            modelMapper.map(userRequest, userInDb);
            if(userRequest.getRoleIds() != null) {
                userInDb.setRoles(roleRepo.findAllById(userRequest.getRoleIds()));
            }
            if(userRequest.getRoleNames() != null) {
                userInDb.setRoles(roleRepo.findAllByName(userRequest.getRoleNames()));
            }

            MultipartFile imgFile = userRequest.getImageFile();
            userInDb.setPhoto(
                    imgFile == null ? userInDb.getPhoto() :
                            cloudinaryImageService.upload(imgFile, CloudinaryUploadFolderConstant.USER_IMG_FOLDER)
            );
            if(!userRequest.getPassword().isEmpty()) {
                userInDb.setPassword(encodePassword(userRequest.getPassword()));
            }
            return userInDb;
        }
    }

    @Override
    public UserDto transformEntityToDto(User userEntity) {
        return null;
    }
}
