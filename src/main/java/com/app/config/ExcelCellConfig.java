package com.app.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExcelCellConfig {
    private Integer columnIndex;
    private String fieldName;
}
