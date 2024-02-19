package com.app.controller;

import com.app.entity.User;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;
import com.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserAPI extends GenericController<User, UserDto, UserRequest> {
    private final UserService userService;

    public UserAPI(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUserDetails() {
        UserDto res = userService.retrieveCurrentUser();
        return ResponseEntity.ok(res);
    }
}
