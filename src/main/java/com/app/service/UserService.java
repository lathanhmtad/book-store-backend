package com.app.service;

import com.app.entity.User;
import com.app.payload.user.UserDto;

public interface UserService extends AbstractBaseService<User, UserDto> {
    UserDto retrieveCurrentUser();
}
