package com.book.service.impl;

import com.book.entity.User;
import com.book.payload.UserDto;
import com.book.payload.UserRequest;
import com.book.repository.UserRepo;
import com.book.service.CloudinaryImageService;
import com.book.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private CloudinaryImageService cloudinaryImageService;
    private ModelMapper modelMapper;
    UserRepo userRepo;
    @Override
    public UserDto createNewUser(UserRequest userRequest) {
        User userEntity = modelMapper.map(userRequest, User.class);

        return null;
    }
}
