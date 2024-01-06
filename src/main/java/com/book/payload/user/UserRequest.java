package com.book.payload.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UserRequest extends UserDto {
    private String password;
    private MultipartFile image;
    private List<Long> roleIds;
}
