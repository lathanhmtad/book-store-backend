package com.book.service.impl;

import com.book.entity.Role;
import com.book.exception.BookStoreApiException;
import com.book.exception.ResourceNotFoundException;
import com.book.payload.RoleDto;
import com.book.payload.RoleResponse;
import com.book.repository.RoleRepo;
import com.book.service.RoleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private ModelMapper modelMapper;
    private RoleRepo roleRepo;

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
    public void createRoles(List<RoleDto> roles) {
        try {
            List<Role> entities = roles.stream().map(role -> modelMapper.map(role, Role.class)).collect(Collectors.toList());
            roleRepo.saveAll(entities);
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public void update(Long id, RoleDto roleDto) {
        try {
            Role existedRole = roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", String.valueOf(id)));
            modelMapper.map(roleDto, existedRole);
            roleRepo.save(existedRole);
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void delete(List<Long> ids) {
        try {
            roleRepo.deleteAllById(ids);
        } catch (Exception e) {
            throw new BookStoreApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}