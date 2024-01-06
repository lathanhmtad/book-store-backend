package com.book.service.impl;

import com.book.constant.CloudinaryUploadFolderConstant;
import com.book.entity.User;
import com.book.exception.BookStoreApiException;
import com.book.payload.user.UserDto;
import com.book.payload.user.UserRequest;
import com.book.repository.UserRepo;
import com.book.service.CloudinaryImageService;
import com.book.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private CloudinaryImageService cloudinaryImageService;
    private ModelMapper modelMapper;
    private UserRepo userRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto createNewUser(UserRequest userRequest) {
        try {
            User userEntity = modelMapper.map(userRequest, User.class);
            MultipartFile multipartFile = userRequest.getImage();
            String imgUrl = cloudinaryImageService.upload(multipartFile, CloudinaryUploadFolderConstant.USER_IMG_FOLDER);
            userEntity.setPhoto(imgUrl);
            User savedUser = userRepo.save(userEntity);

            return modelMapper.map(savedUser, UserDto.class);
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
