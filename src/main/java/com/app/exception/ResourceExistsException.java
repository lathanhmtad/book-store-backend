package com.app.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceExistsException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private String fieldValue;

    public ResourceExistsException
            (String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s with %s '%s' already exists. Please choose a different %s.", resourceName, fieldName, fieldValue, fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}


