package com.app.service;

import com.app.payload.role.RoleDto;
import com.app.payload.role.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleDto> listAll();
    RoleResponse listAll(Integer page, Integer limit);
    List<RoleDto> createRoles(List<RoleDto> roles);
    RoleDto update(Long id, RoleDto roleDto);
    void delete(List<Long> ids);
    void delete(Long id);
}
