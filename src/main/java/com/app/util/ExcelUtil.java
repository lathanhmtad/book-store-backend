package com.app.util;

import com.app.config.ExcelCellConfig;
import com.app.config.ExcelImportConfig;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExcelUtil {
    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    public static <T> List<T> getImportData(Workbook workbook, ExcelImportConfig importConfig) {
        List<T> list = new ArrayList<>();

        List<ExcelCellConfig> cellConfigs = importConfig.getCellImportConfigs();
        Sheet sheet = workbook.getSheet(importConfig.getSheetName());

        int countRow = 0;
        for(Row row : sheet) {
            if(countRow < importConfig.getStartRow()) {
                countRow++;
                continue;
            }

            if(isEmptyRow(row)) {
                break;
            }

            T rowData = getRowData(row, cellConfigs, importConfig.getDataClazz());
            list.add(rowData);
            countRow++;
        }
        return list;
    }

    private static boolean isEmptyRow(Row row) {
        for(Cell cell : row) {
            if(cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private static <T> T getRowData(Row row, List<ExcelCellConfig> cellConfigs, Class dataClazz) {
        T instance = null;
        try {
            instance = (T) dataClazz.getDeclaredConstructor().newInstance();

            for(int i = 0; i < cellConfigs.size(); i++) {
                ExcelCellConfig currentCell = cellConfigs.get(i);
                try {
                    Field field = getDeclaredField(dataClazz, currentCell.getFieldName());

                    Cell cell = row.getCell(currentCell.getColumnIndex());
                    if(!ObjectUtils.isEmpty(cell)) {
                        cell.setCellType(CellType.STRING);
                        Object cellValue = cell.getStringCellValue();
                        setFieldValue(instance, field, cellValue);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    private static Field getDeclaredField(Class clazz, String fieldName) {
        if(ObjectUtils.isEmpty(clazz) || ObjectUtils.isEmpty(fieldName)) {
            return null;
        }
        do {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {

            }
        } while ((clazz = clazz.getSuperclass()) != null);
        return null;
    }
    private static <T> void setFieldValue(Object instance, Field field, Object cellValue) {
        if (ObjectUtils.isEmpty(instance) || ObjectUtils.isEmpty(field)) {
            return;
        }
        Class clazz = field.getType();

        Object valueConverted = parseValueByClass(clazz, cellValue);

        try {
            field.set(instance, valueConverted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object parseValueByClass(Class clazz, Object cellValue) {
        if (ObjectUtils.isEmpty(clazz) || ObjectUtils.isEmpty(cellValue)) {
            return null;
        }
        String clazzName = clazz.getSimpleName();
        return switch (clazzName) {
            case "String" -> cellValue.toString().trim();
            case "Boolean", "boolean" -> parseBoolean(cellValue);
            case "List" -> parseList(cellValue);
            default ->  null;
        };
    }

    private static Object parseBoolean(Object value) {
        return ObjectUtils.isEmpty(value) ? null : Boolean.valueOf(value.toString());
    }

    private static Object parseList(Object value) {
        Object[] stringObj = value.toString().split(",");
        return Arrays.asList(stringObj);
    }
}
