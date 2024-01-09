package com.book.service;

import com.book.payload.user.UserDto;
import com.book.payload.user.UserRequest;
import com.book.payload.user.UserResponse;

public interface UserService {
    UserDto createNewUser(UserRequest userRequest);
    UserResponse getUsers(Integer pageNumber, Integer limit);
    void deleteUser(Long id);
}
