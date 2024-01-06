package com.book.controller;

import com.book.payload.user.UserDto;
import com.book.payload.user.UserRequest;
import com.book.service.UserService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createNewUser(@ModelAttribute UserRequest userRequest) {
        UserDto response = userService.createNewUser(userRequest);
        return ResponseEntity.ok(response);
    }
}
