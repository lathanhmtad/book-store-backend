package com.app.service.impl;

import com.app.entity.Role;
import com.app.exception.BookStoreApiException;
import com.app.exception.ResourceNotFoundException;
import com.app.payload.role.RoleDto;
import com.app.payload.role.RoleResponse;
import com.app.repository.RoleRepo;
import com.app.service.RoleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private ModelMapper modelMapper;
    private RoleRepo roleRepo;

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
    public RoleResponse listAll(Integer pageNumber, Integer limit) {
        try {
            Sort sort = Sort.by("id").descending();
            Pageable pageable = PageRequest.of(pageNumber - 1, limit, sort);
            Page<Role> page = roleRepo.findAll(pageable);

            List<RoleDto> roles = page.getContent().stream().map(item -> modelMapper.map(item, RoleDto.class)).collect(Collectors.toList());

            return RoleResponse.builder()
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
    public List<RoleDto> createRoles(List<RoleDto> roles) {
        try {
            List<Role> entities = roles.stream().map(role -> modelMapper.map(role, Role.class)).collect(Collectors.toList());
            List<Role> savedRoles = roleRepo.saveAll(entities);
            return savedRoles.stream().map(entity -> modelMapper.map(entity, RoleDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
            roleRepo.deleteById(existedRole.getId());
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
