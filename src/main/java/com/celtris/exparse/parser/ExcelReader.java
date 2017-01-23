/*******************************************************************************
 * Copyright 2017 Celtris Tech India Private Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.celtris.exparse.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.celtris.exparse.models.SheetData;

/**
 * @author Pratick Chokhani <pratick@celtris.com>
 *
 */
public class ExcelReader<T> {

	public List<SheetData<T>> readExcel(String absolutePath, Class<T> excelModelClass, boolean headerExtraction)
			throws IOException, InstantiationException, IllegalAccessException {

		FileInputStream file = new FileInputStream(new File(absolutePath));

		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		DataFormatter objDefaultFormat = new DataFormatter();
		FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator(workbook);

		Iterator<Sheet> sheetIterator = workbook.iterator();
		List<SheetData<T>> sheetDataList = new ArrayList<SheetData<T>>(workbook.getNumberOfSheets());
		int sheetCount = 0;
		while (sheetIterator.hasNext()) {
			sheetCount++;

			ExcelParser<T> excelParser = new ExcelParser<T>(headerExtraction, excelModelClass);
			Sheet sheet = sheetIterator.next();
			Iterator<Row> rowIterator = sheet.iterator();

			int rowCount = 0;

			// Evaluating header
			if (headerExtraction) {
				if (rowIterator.hasNext()) {

					rowCount++;

					Field[] fields = excelModelClass.getFields();
					List<String> heaaderStr = new ArrayList<String>(fields.length);

					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();

					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						String cellStrValue = cell.getStringCellValue();

						heaaderStr.add(cellStrValue);
					}
					excelParser.processFieldAccordingToHeader(heaaderStr, sheet.getSheetName());
				}
			}

			while (rowIterator.hasNext()) {
				rowCount++;
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				List<String> rowStr = new ArrayList<String>(excelParser.parameterCount());
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					String cellStrValue = "";
					switch (cell.getCellTypeEnum()) {
					case STRING:
						cellStrValue = cell.getStringCellValue();
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							objFormulaEvaluator.evaluate(cell);
							cellStrValue = objDefaultFormat.formatCellValue(cell, objFormulaEvaluator);
						} else {

							cellStrValue = Double.toString(cell.getNumericCellValue());
						}
						break;
					case BOOLEAN:
						cellStrValue = Boolean.toString(cell.getBooleanCellValue());
						break;
					case FORMULA:
						cellStrValue = cell.getStringCellValue();
						break;
					case BLANK:

					default:
						break;
					}
					rowStr.add(cellStrValue);
				}

				excelParser.processRow(rowStr, rowCount, sheet.getSheetName());
			}

			SheetData<T> sheetData = new SheetData<T>(excelParser.getParsedObject(), sheet.getSheetName(), sheetCount);
			sheetDataList.add(sheetData);
		}

		file.close();
		workbook.close();
		return sheetDataList;
	}
}
