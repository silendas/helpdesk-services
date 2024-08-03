package com.cms.helpdesk.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReportExcelUtil {

    public ResponseEntity<InputStreamResource> generate(String sheetName, List<String> headers, List<Map<String, Object>> data) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
        }

        // Fill data rows
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

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            workbook.close();

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            InputStreamResource file = new InputStreamResource(in);

            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.add("Content-Disposition", "attachment; filename=" + sheetName + ".xlsx");
            
            return ResponseEntity.ok()
                    .headers(headersResponse)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(file);

        } catch (IOException e) {
            log.error("Error while writing to Excel file", e);
            return ResponseEntity.status(500).build();
        }
    }
}
