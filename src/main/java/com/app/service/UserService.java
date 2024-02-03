package com.app.service;

import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserCreationDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDto getUserById(Long userId);
    UserDto retrieveCurrentUser();
    PaginationResponse<UserDto> getUsers(Integer pageNumber, Integer limit);
    BaseResponse importUserData(MultipartFile importFile);
    BaseResponse saveUser(UserCreationDto userRequest);
    BaseResponse deleteUser(Long id);
}
