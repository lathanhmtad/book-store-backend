package com.app.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDto extends AuditableDto<Long> {
    private Long id;
}
