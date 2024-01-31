package com.app.config;

import com.app.payload.category.CategoryRequest;
import com.app.payload.user.UserRequest;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportConfig {
    private String sheetName;
    private Integer headerIndex;
    private Integer startRow;
    private Class dataClazz;
    private List<ExcelCellConfig> cellImportConfigs;

    public static ExcelImportConfig userImportConfig;
    public static ExcelImportConfig categoryImportConfig;

    static {
        userImportConfig = ExcelImportConfig.builder()
                .sheetName("Users")
                .headerIndex(0)
                .startRow(1)
                .dataClazz(UserRequest.class)
                .build();

        List<ExcelCellConfig> userCellConfigs = List.of(
                new ExcelCellConfig(0, "email"),
                new ExcelCellConfig(1, "fullName"),
                new ExcelCellConfig(2, "password"),
                new ExcelCellConfig(3, "roleNames"),
                new ExcelCellConfig(4, "enabled")
        );

        userImportConfig.setCellImportConfigs(userCellConfigs);
    }

    static {
        categoryImportConfig = ExcelImportConfig.builder()
                .sheetName("Categories")
                .headerIndex(0)
                .startRow(1)
                .dataClazz(CategoryRequest.class)
                .build();
        List<ExcelCellConfig> categoryCellConfigs = List.of(
                new ExcelCellConfig(0, "name"),
                new ExcelCellConfig(1, "enabled"),
                new ExcelCellConfig(2, "parentName")
        );
        categoryImportConfig.setCellImportConfigs(categoryCellConfigs);
    }
}
