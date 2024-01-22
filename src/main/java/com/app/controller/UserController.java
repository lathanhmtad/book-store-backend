package com.app.controller;

import com.app.constant.AppConstant;
import com.app.payload.PaginationResponse;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;
import com.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<PaginationResponse<UserDto>> getUsers(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "limit", defaultValue = AppConstant.USER_DEFAULT_PAGE_SIZE) Integer limit
    ) {
        PaginationResponse<UserDto> response = userService.getUsers(page, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId) {
        UserDto res = userService.getUserById(userId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUserDetails() {
        UserDto res = userService.retrieveCurrentUser();
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<UserDto> createNewUser(@ModelAttribute UserRequest userRequest) {
        UserDto response = userService.createNewUser(userRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "delete user success"));
    }
}
