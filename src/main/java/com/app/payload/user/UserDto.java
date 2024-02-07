package com.app.payload.user;

import com.app.payload.BaseDto;
import com.app.payload.role.RoleDto;
import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto extends BaseDto {
    private String email;
    private String fullName;
    private String photo;
    private Boolean enabled;
    private List<RoleDto> roles;
}
