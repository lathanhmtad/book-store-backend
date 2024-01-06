package com.book.service;

import com.book.payload.user.UserDto;
import com.book.payload.user.UserRequest;

public interface UserService {
    UserDto createNewUser(UserRequest userRequest);

}
