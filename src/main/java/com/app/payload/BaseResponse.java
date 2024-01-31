package com.app.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponse {
    private Integer code;
    private String message;
}
