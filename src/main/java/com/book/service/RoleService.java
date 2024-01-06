package com.book.service;

import com.book.payload.role.RoleDto;
import com.book.payload.role.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleDto> listAll();
    RoleResponse listAll(Integer page, Integer limit);
    List<RoleDto> createRoles(List<RoleDto> roles);
    RoleDto update(Long id, RoleDto roleDto);
    void delete(List<Long> ids);
    void delete(Long id);
}
