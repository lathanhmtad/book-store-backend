package com.book.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RoleResponse {
    private Long id;
    private String name;
    private String description;
}
