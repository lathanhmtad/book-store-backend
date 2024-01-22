package com.app.payload.role;

import com.app.payload.AuditableDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto extends AuditableDto<Long> {
    private Long id;
    private String name;
    private String description;
}
