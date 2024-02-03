package com.app.controller;

import com.app.payload.role.RoleDto;
import com.app.service.RoleService;
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
public class RoleAPI {
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        List<RoleDto> response = roleService.getRoles();

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.maxAge(2, TimeUnit.MINUTES));
        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }
}
