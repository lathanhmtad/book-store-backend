package com.app.controller;

import com.app.entity.Privilege;
import com.app.repository.PrivilegeRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/permissions")
public class PrivilegeController {
    private PrivilegeRepo privilegeRepo;
    @GetMapping("/all")
    ResponseEntity<List<Privilege>> getAll() {
        return ResponseEntity.ok(privilegeRepo.findAll());
    }
}
