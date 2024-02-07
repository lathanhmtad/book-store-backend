package com.app.controller;

import com.app.entity.User;
import com.app.payload.BaseResponse;
import com.app.payload.user.UserDto;
import com.app.payload.user.UserRequest;
import com.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
public class UserAPI extends GenericController<User, UserDto> {
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

    @PostMapping("/import")
    public ResponseEntity<BaseResponse> importUserData(@RequestParam("file") MultipartFile importFile) {
        BaseResponse baseResponse = userService.importUserData(importFile);
        return ResponseEntity.ok(baseResponse);
    }

//    @PostMapping
//    public ResponseEntity<BaseResponse> createNewUser(@ModelAttribute UserRequest userRequest) {
//        BaseResponse baseResponse = userService.saveOrUpdate(userRequest);
//        return ResponseEntity.ok(baseResponse);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateUser(@PathVariable("id") Long userId, @ModelAttribute UserRequest userRequest) {
//        userRequest.setId(userId);
//        BaseResponse baseResponse = userService.saveUser(userRequest);
//        return ResponseEntity.ok(baseResponse);
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable Long id) {
        BaseResponse baseResponse = userService.deleteUser(id);
        return ResponseEntity.ok(baseResponse);
    }
}
