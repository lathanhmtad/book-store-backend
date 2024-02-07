package com.app.service;

import com.app.entity.User;
import com.app.payload.BaseResponse;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends AbstractBaseService<User, UserDto> {
    UserDto retrieveCurrentUser();
    BaseResponse importUserData(MultipartFile importFile);
//    BaseResponse saveUser(UserRequest userRequest);
    BaseResponse deleteUser(Long id);
}
