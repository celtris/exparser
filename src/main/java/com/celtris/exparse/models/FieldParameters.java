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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.joda.time.format.DateTimeFormatter;

/**
 * @author Pratick Chokhani <pratick@celtris.com>
 *
 */
public class FieldParameters {

	private Field field;
	private Class<?> fieldType;
	private String[] nulls;
	private boolean lowerCase;
	private boolean trim;
	private String defaultNullValue;
	private String dateTimeFormatterStr;
	private DateTimeFormatter dateTimeFormatter;
	private final Method writeMethod;
	private boolean required;

	/**
	 * @param field
	 * @param fieldType
	 * @param required
	 * @param nulls
	 * @param lowerCase
	 * @param trim
	 */
	public FieldParameters(Field field, Class<?> fieldType, String[] nulls, boolean lowerCase, boolean trim,
			String defaultNullValue, String dateTimeFormatterStr, DateTimeFormatter dateTimeFormatter, boolean required,
			PropertyDescriptor property) {
		super();
		this.field = field;
		this.fieldType = fieldType;
		this.nulls = nulls;
		this.lowerCase = lowerCase;
		this.trim = trim;
		this.defaultNullValue = defaultNullValue;
		this.dateTimeFormatterStr = dateTimeFormatterStr;
		this.dateTimeFormatter = dateTimeFormatter;
		this.required = required;
		this.writeMethod = property != null ? property.getWriteMethod() : null;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Class<?> getFieldType() {
		return fieldType;
	}

	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}

	public String[] getNulls() {
		return nulls;
	}

	public void setNulls(String[] nulls) {
		this.nulls = nulls;
	}

	public boolean isLowerCase() {
		return lowerCase;
	}

	public void setLowerCase(boolean lowerCase) {
		this.lowerCase = lowerCase;
	}

	public boolean isTrim() {
		return trim;
	}

	public void setTrim(boolean trim) {
		this.trim = trim;
	}

	public String getDefaultNullValue() {
		return defaultNullValue;
	}

	public void setDefaultNullValue(String defaultNullValue) {
		this.defaultNullValue = defaultNullValue;
	}

	public String getDateTimeFormatterStr() {
		return dateTimeFormatterStr;
	}

	public void setDateTimeFormatterStr(String dateTimeFormatterStr) {
		this.dateTimeFormatterStr = dateTimeFormatterStr;
	}

	public DateTimeFormatter getDateTimeFormatter() {
		return dateTimeFormatter;
	}

	public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
		this.dateTimeFormatter = dateTimeFormatter;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public void write(Object instance, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		if(writeMethod==null) {
			field.set(instance, value);
		} else {
			writeMethod.invoke(instance, value);
		}
	}

}
