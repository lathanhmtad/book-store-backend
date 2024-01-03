package com.book.service;

import com.book.payload.RoleDto;
import com.book.payload.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse listAll(Integer page, Integer limit);
    void createRoles(List<RoleDto> roles);
    void update(Long id, RoleDto roleDto);
    void delete(List<Long> ids);
}
