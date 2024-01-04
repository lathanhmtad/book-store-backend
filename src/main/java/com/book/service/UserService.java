package com.book.service;

import com.book.payload.UserDto;
import com.book.payload.UserRequest;

public interface UserService {
    UserDto createNewUser(UserRequest userRequest);

}
