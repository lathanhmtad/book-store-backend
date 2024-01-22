package com.app.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditableDto<U> {
    protected U createdBy;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    protected Date createdDate;

    protected U lastModifiedBy;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    protected Date lastModifiedDate;
}
