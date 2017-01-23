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
package com.celtris.exparse.main;

import java.io.IOException;
import java.util.List;

import com.celtris.exparse.models.ExcelExample;
import com.celtris.exparse.models.SheetData;
import com.celtris.exparse.parser.ExcelReader;

/**
 * @author Pratick Chokhani <pratick@celtris.com>
 *
 */
public class Main {

	private Main() {
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {

		if (args.length == 0) {

		}

		// String absolutePath = args[0];
		ExcelReader<ExcelExample> excelReader = new ExcelReader<>();
		List<SheetData<ExcelExample>> sheetDataList = excelReader
				.readExcel("/Users/PratickChokhani/Downloads/example.xlsx", ExcelExample.class, true);

		System.out.println(sheetDataList);
	}

}
