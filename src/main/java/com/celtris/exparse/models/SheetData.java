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
package com.celtris.exparse.models;

import java.util.List;

/**
 * @author Pratick Chokhani <pratick@celtris.com>
 *
 */
public class SheetData<T> {

	private List<T> parsedObject;
	private String sheetName;
	private int sheetNo;

	/**
	 * @param parsedObject
	 * @param sheetName
	 * @param sheetNo
	 */
	public SheetData(List<T> parsedObject, String sheetName, int sheetNo) {
		super();
		this.parsedObject = parsedObject;
		this.sheetName = sheetName;
		this.sheetNo = sheetNo;
	}

	public List<T> getParsedObject() {
		return parsedObject;
	}

	public void setParsedObject(List<T> parsedObject) {
		this.parsedObject = parsedObject;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getSheetNo() {
		return sheetNo;
	}

	public void setSheetNo(int sheetNo) {
		this.sheetNo = sheetNo;
	}

	@Override
	public String toString() {
		return "SheetData [parsedObject=" + parsedObject + ", sheetName=" + sheetName + ", sheetNo=" + sheetNo + "]";
	}

}
