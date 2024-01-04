package com.book.controller;

import com.book.payload.UserDto;
import com.book.payload.UserRequest;
import com.book.payload.UserResponse;
import com.book.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;
    @PostMapping
    public ResponseEntity<UserDto> createNewUser(@ModelAttribute UserRequest userRequest) {
//        UserDto response = userService.createNewUser(userRequest);
//        return ResponseEntity.ok(response);
        return null;
    }
}
