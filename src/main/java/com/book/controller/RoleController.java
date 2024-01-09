package com.book.controller;

import com.book.constant.AppConstant;
import com.book.payload.role.RoleDto;
import com.book.payload.role.RoleRequest;
import com.book.payload.role.RoleResponse;
import com.book.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<RoleDto>> getRoles() {
        List<RoleDto> roles = roleService.listAll();
        return ResponseEntity.ok(roles);
    }
    @GetMapping
    public ResponseEntity<RoleResponse> getRoles(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) Integer page,
            @RequestParam(value = "limit", defaultValue = AppConstant.ROLES_DEFAULT_PAGE_SIZE, required = false) Integer limit
    ) {
        RoleResponse result = roleService.listAll(page, limit);
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<List<RoleDto>> createRoles(@RequestBody RoleRequest roleRequest) {
        List<RoleDto> savedRoles = roleService.createRoles(roleRequest.getRoles());
        return ResponseEntity.ok(savedRoles);
    }

    @PutMapping("/{id}")
    ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @RequestBody RoleDto data) {
        RoleDto updatedRole = roleService.update(id, data);
        return ResponseEntity.ok(updatedRole);
    }
    @DeleteMapping
    ResponseEntity<Map<String, String>> deleteRoles(@RequestBody List<Long> ids) {
        roleService.delete(ids);
        return ResponseEntity.ok(Map.of("message", "delete roles success"));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, String>> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.ok(Map.of("message", "delete role success"));
    }
}
