package com.app.service.impl;

import com.app.entity.Privilege;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.exception.BookStoreApiException;
import com.app.exception.ResourceNotFoundException;
import com.app.payload.PaginationResponse;
import com.app.payload.role.RoleDto;
import com.app.repository.PrivilegeRepo;
import com.app.repository.RoleRepo;
import com.app.repository.UserRepo;
import com.app.service.RoleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private ModelMapper modelMapper;
    private RoleRepo roleRepo;
    private UserRepo userRepo;
    private PrivilegeRepo privilegeRepo;

    @Override
    public List<RoleDto> listAll() {
        try {
            List<Role> roles = roleRepo.findAll();
            return roles.stream().map(item -> modelMapper.map(item, RoleDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLD:READ')")
    public PaginationResponse<RoleDto> listAll(Integer pageNumber, Integer limit) {
        try {
            Sort sort = Sort.by("id").descending();
            Pageable pageable = PageRequest.of(pageNumber - 1, limit, sort);
            Page<Role> page = roleRepo.findAll(pageable);

            List<RoleDto> roles = page.getContent().stream().map(item -> modelMapper.map(item, RoleDto.class)).collect(Collectors.toList());

            return PaginationResponse.<RoleDto>builder()
                    .data(roles)
                    .pageNumber(pageNumber)
                    .pageSize(limit)
                    .totalPages(page.getTotalPages())
                    .totalElements((int) page.getTotalElements())
                    .last(page.isLast())
                    .build();
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public RoleDto getRoleById(Long roleId) {
        Role entity = roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", String.valueOf(roleId)));
        return modelMapper.map(entity, RoleDto.class);
    }

    @Override
    public List<RoleDto> createRoles(List<RoleDto> roles) {
        try {
            List<Role> entities = roles.stream().map(role -> modelMapper.map(role, Role.class)).collect(Collectors.toList());
            List<Role> savedRoles = roleRepo.saveAll(entities);
            return savedRoles.stream().map(entity -> modelMapper.map(entity, RoleDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BookStoreApiException("There is a role you created previously", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void assignPermission(Long roleId, List<Long> permissions) {
        try {
            Role role = roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", String.valueOf(roleId)));
            List<Privilege> privileges =  privilegeRepo.findAllById(permissions);
            role.setPrivileges(privileges);
            roleRepo.save(role);
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<Privilege> getPermissionByRoleId(Long roleId) {
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", String.valueOf(roleId)));
        return role.getPrivileges().stream().toList();
    }

    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        try {
            Role existedRole = roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", String.valueOf(id)));
            roleDto.setId(existedRole.getId());
            modelMapper.map(roleDto, existedRole);
            Role savedRole = roleRepo.save(existedRole);
            return modelMapper.map(savedRole, RoleDto.class);
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void delete(List<Long> ids) {
        try {
            ids.forEach(id -> {
                roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", String.valueOf(id)));
                roleRepo.deleteById(id);
            });
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Role existedRole = roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", String.valueOf(id)));
            for(User user : existedRole.getUsers()) {
                user.getRoles().remove(existedRole);
                userRepo.save(user);
            }
            roleRepo.deleteById(existedRole.getId());
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
