package com.app.service;

import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;
import com.app.payload.user.UserResponse;

public interface UserService {
    UserDto createNewUser(UserRequest userRequest);
    UserResponse getUsers(Integer pageNumber, Integer limit);
    void deleteUser(Long id);
}
