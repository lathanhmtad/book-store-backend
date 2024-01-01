package com.book.service.impl;

import com.book.entity.User;
import com.book.payload.UserDto;
import com.book.payload.UserRequest;
import com.book.repository.UserRepo;
import com.book.service.CloudinaryImageService;
import com.book.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private CloudinaryImageService cloudinaryImageService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    UserRepo userRepo;
    @Override
    public UserDto createNewUser(UserRequest userRequest) {
        User userEntity = modelMapper.map(userRequest, User.class);

        return null;
    }
}
