package com.app.service.impl;

import com.app.entity.Role;
import com.app.payload.role.RoleDto;
import com.app.repository.RoleRepo;
import com.app.service.RoleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepo roleRepo;
    private ModelMapper modelMapper;

    @Override
    public List<RoleDto> getRoles() {
        List<Role> roles = roleRepo.findAll();
        return roles.stream().map(role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toList());
    }
}
