package com.book.payload.user;

import com.book.payload.role.RoleDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String photo;
    private Boolean enabled;
    private List<RoleDto> roles;
}
