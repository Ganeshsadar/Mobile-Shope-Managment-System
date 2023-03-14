package com.averta.inventory.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import com.averta.inventory.entity.Product;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelHelper {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Id", "Product name", "brand", "type", "Gst", "Color", "unit", "volume", "Hsn code",
			"rate", "quantity" };
	static String SHEET = "Products";

	public static boolean hasExcelFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<Product> excelToProduct(InputStream is) {
		try {
			Workbook workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();
			List<Product> tutorials = new ArrayList<Product>();
			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();
				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}
				Iterator<Cell> cellsInRow = currentRow.iterator();
				Product produce = new Product();
				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					switch (cellIdx) {
					case 0:
						produce.setProductId((long) currentCell.getNumericCellValue());
						break;
					case 1:
						produce.setProductName(currentCell.getStringCellValue());
						break;
					case 2:
						produce.setProductGst(currentCell.getNumericCellValue());
						break;

					case 3:
						produce.setProductUnit(currentCell.getStringCellValue());
						break;
					default:
						break;
					}
					cellIdx++;
				}
				tutorials.add(produce);
			}
			((InputStream) workbook).close();
			return tutorials;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
}
