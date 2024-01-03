package com.book.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleRequest {
    private List<RoleDto> roles;
}
