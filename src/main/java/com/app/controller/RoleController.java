package com.app.controller;

import com.app.entity.Role;
import com.app.repository.RoleRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private RoleRepo roleRepo;

    @GetMapping
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = roleRepo.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.maxAge(2, TimeUnit.MINUTES));
        return ResponseEntity.ok()
                .headers(headers)
                .body(roles);
    }
}
