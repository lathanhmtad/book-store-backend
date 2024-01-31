package com.app.service;

import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    PaginationResponse<UserDto> getUsers(Integer pageNumber, Integer limit);
    UserDto getUserById(Long userId);
    UserDto retrieveCurrentUser();
    BaseResponse createNewUser(UserRequest userRequest);
    BaseResponse importUserData(MultipartFile importFile);
    BaseResponse deleteUser(Long id);
}
