package com.book.controller;

import com.book.payload.UserDto;
import com.book.payload.UserRequest;
import com.book.payload.UserResponse;
import com.book.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<?> createNewUser() {
//        UserDto response = userService.createNewUser(userRequest);
//        return ResponseEntity.ok(response);
        return ResponseEntity.ok("Hi");
    }
}
