package com.app.util;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public class FileFactory {
    public static Workbook getWorkbookStream(MultipartFile importFile) {
        InputStream inputStream = null;

        try {
            inputStream = importFile.getInputStream();

            Workbook workbook = WorkbookFactory.create(inputStream);

            return workbook;
        } catch (Exception e) {
            return null;
        }
    }
}
