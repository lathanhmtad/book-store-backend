package com.app.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditableDto<U> {
    protected U createdBy;

    protected Date createdDate;

    protected U lastModifiedBy;

    protected Date lastModifiedDate;
}
