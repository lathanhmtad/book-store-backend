package com.app.service.impl;

import com.app.constant.CloudinaryUploadFolderConstant;
import com.app.entity.User;
import com.app.exception.BookStoreApiException;
import com.app.exception.ResourceNotFoundException;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;
import com.app.payload.user.UserResponse;
import com.app.repository.RoleRepo;
import com.app.repository.UserRepo;
import com.app.service.CloudinaryImageService;
import com.app.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    private CloudinaryImageService cloudinaryImageService;
    private ModelMapper modelMapper;
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto createNewUser(UserRequest userRequest) {
        try {
            User userEntity = modelMapper.map(userRequest, User.class);
            userEntity.setRoles(roleRepo.findAllById(userRequest.getRoleIds()));
            MultipartFile multipartFile = userRequest.getImage();
            String imgUrl = cloudinaryImageService.upload(multipartFile, CloudinaryUploadFolderConstant.USER_IMG_FOLDER);
            userEntity.setPhoto(imgUrl);
            userEntity.setPassword(encodePassword(userEntity.getPassword()));
            User savedUser = userRepo.save(userEntity);

            return modelMapper.map(savedUser, UserDto.class);
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public UserResponse getUsers(Integer pageNumber, Integer limit) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, limit, sort);
        Page<User> page = userRepo.findAll(pageable) ;

        List<User> users = page.getContent();

        UserResponse response = UserResponse.builder()
                .data(users.stream().map(item -> modelMapper.map(item, UserDto.class)).collect(Collectors.toList()))
                .totalPages(page.getTotalPages())
                .totalElements((int) page.getTotalElements())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .last(page.isLast())
                .build();
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        try {
             User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(id)));
             cloudinaryImageService.delete(user.getPhoto(), CloudinaryUploadFolderConstant.USER_IMG_FOLDER);
             userRepo.deleteById(id);
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
