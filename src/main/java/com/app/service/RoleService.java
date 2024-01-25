package com.app.service;

import com.app.entity.Privilege;
import com.app.payload.PaginationResponse;
import com.app.payload.role.RoleDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RoleService {
    @PreAuthorize("hasAuthority('ROLD:READ')")
    List<RoleDto> listAll();

    PaginationResponse<RoleDto> listAll(Integer page, Integer limit);
    RoleDto getRoleById(Long roleId);
    List<RoleDto> createRoles(List<RoleDto> roles);
    void assignPermission(Long roleId, List<Long> permissions);
    List<Privilege> getPermissionByRoleId(Long roleId);
    RoleDto update(Long id, RoleDto roleDto);
    void delete(List<Long> ids);
    void delete(Long id);
}
