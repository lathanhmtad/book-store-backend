package com.app.controller;

import com.app.constant.AppConstant;
import com.app.payload.BaseResponse;
import com.app.payload.PaginationResponse;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserCreationDto;
import com.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserAPI {
    private UserService userService;

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

    @GetMapping
    public ResponseEntity<PaginationResponse<UserDto>> getUsers(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "limit", defaultValue = AppConstant.USER_DEFAULT_PAGE_SIZE) Integer limit
    ) {
        PaginationResponse<UserDto> response = userService.getUsers(page, limit);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    public ResponseEntity<BaseResponse> importUserData(@RequestParam("file") MultipartFile importFile) {
        BaseResponse baseResponse = userService.importUserData(importFile);
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createNewUser(@ModelAttribute UserCreationDto userRequest) {
        BaseResponse baseResponse = userService.saveUser(userRequest);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateUser(@PathVariable("id") Long userId, @ModelAttribute UserCreationDto userRequest) {
        userRequest.setId(userId);
        BaseResponse baseResponse = userService.saveUser(userRequest);
        return ResponseEntity.ok(baseResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable Long id) {
        BaseResponse baseResponse = userService.deleteUser(id);
        return ResponseEntity.ok(baseResponse);
    }
}
