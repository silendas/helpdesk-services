package com.cms.helpdesk.common.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReportExcelUtil {

     public void generate(HttpServletResponse response, String sheetName, List<String> headers, List<Map<String, Object>> data) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
        }

        for (int i = 0; i < data.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            Map<String, Object> rowData = data.get(i);
            int cellIndex = 0;
            for (String header : headers) {
                Cell cell = dataRow.createCell(cellIndex++);
                Object value = rowData.get(header);
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                }
            }
        }

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + sheetName + ".xlsx");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            log.error("Error while writing to Excel file", e);
        }
    }

}
