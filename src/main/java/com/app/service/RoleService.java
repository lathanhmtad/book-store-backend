package com.app.service;

import com.app.payload.PaginationResponse;
import com.app.payload.role.RoleDto;

import java.util.List;

public interface RoleService {
    List<RoleDto> listAll();
    PaginationResponse<RoleDto> listAll(Integer page, Integer limit);
    RoleDto getRoleById(Long roleId);
    List<RoleDto> createRoles(List<RoleDto> roles);
    RoleDto update(Long id, RoleDto roleDto);
    void delete(List<Long> ids);
    void delete(Long id);
}
