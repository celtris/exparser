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

import java.io.Serializable;

import org.joda.time.DateTime;

import com.celtris.exparse.annotations.DateTimeFormatPattern;
import com.celtris.exparse.annotations.Parsed;

/**
 * @author Pratick Chokhani <pratick@celtris.com>
 *
 */
public class ExcelExample implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 214350894413187891L;
	@Parsed(field = "name")
	private String name;
	@Parsed(field = "count")
	private int count;
	@DateTimeFormatPattern(pattern="dd-MMM-yyyy")
	@Parsed(field = "lob", requird = true)
	private DateTime lob;

	public ExcelExample() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public DateTime getLob() {
		return lob;
	}

	public void setLob(DateTime lob) {
		this.lob = lob;
	}

	@Override
	public String toString() {
		return "ExcelExample [name=" + name + ", count=" + count + ", lob=" + lob + "]\n";
	}

}
