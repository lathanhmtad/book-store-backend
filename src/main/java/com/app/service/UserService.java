package com.app.service;

import com.app.payload.PaginationResponse;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;

public interface UserService {
    UserDto createNewUser(UserRequest userRequest);
    PaginationResponse<UserDto> getUsers(Integer pageNumber, Integer limit);
    UserDto getUserById(Long userId);
    UserDto retrieveCurrentUser();
    void deleteUser(Long id);
}
