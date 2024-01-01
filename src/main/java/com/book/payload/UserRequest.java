package com.book.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserRequest extends UserDto {
    private String password;
    private MultipartFile image;
}
