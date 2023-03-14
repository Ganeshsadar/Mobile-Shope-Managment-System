package com.averta.inventory.utility;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.averta.inventory.bo.ReportOne;

public class ExcelGenerator1 {
	private List < ReportOne > reportOnes;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGenerator1(List < ReportOne > reportOnes) {
        this.reportOnes = reportOnes;
        workbook = new XSSFWorkbook();

    }
    private void writeHeader() {
        sheet = workbook.createSheet("monthly sells");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Seller name", style);
        createCell(row, 1, "Product name", style);
        createCell(row, 2, "Rate", style);
        createCell(row, 3, "Order quantity", style);
        createCell(row, 4, "Available", style);
        

    }
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }
    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (ReportOne record: reportOnes) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getsellername().toString(), style);
            createCell(row, columnCount++, record.getproductname().toString(), style);
            createCell(row, columnCount++, record.getproductRate().toString(), style);
            createCell(row, columnCount++, record.getorderedQuantity().toString(), style);
            createCell(row, columnCount++, record.getavailableQuantity().toString(), style);

        }

    }
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
       
        outputStream.close();

}
}
