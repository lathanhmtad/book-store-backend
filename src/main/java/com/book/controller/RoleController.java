package com.book.controller;

import com.book.payload.RoleDto;
import com.book.payload.RoleRequest;
import com.book.payload.RoleResponse;
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
    @GetMapping
    public ResponseEntity<RoleResponse> getRoles(
            @RequestParam(value = "page", defaultValue = "4", required = false) Integer page,
            @RequestParam(value = "limit", defaultValue = "4", required = false) Integer limit
    ) {
        RoleResponse result = roleService.listAll(page, limit);
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<Map<String, String>> createRoles(@RequestBody RoleRequest roleRequest) {
        roleService.createRoles(roleRequest.getRoles());
        return ResponseEntity.ok(Map.of("message", "create new success roles"));
    }

    @PutMapping("/{id}")
    ResponseEntity<Map<String, String>> updateRole(@PathVariable Long id, @RequestBody RoleDto data) {
        roleService.update(id, data);
        return ResponseEntity.ok(Map.of("message", "update role success"));
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
