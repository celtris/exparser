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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.rmi.UnexpectedException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.celtris.exparse.annotations.DateTimeFormatPattern;
import com.celtris.exparse.annotations.LowerCase;
import com.celtris.exparse.annotations.NullString;
import com.celtris.exparse.annotations.Parsed;
import com.celtris.exparse.annotations.Trim;
import com.celtris.exparse.models.FieldParameters;
import com.celtris.exparse.utils.Conversion;

/**
 * @author Pratick Chokhani <pratick@celtris.com>
 *
 */
public class ExcelParser<T> {

	private boolean headerExtraction;
	private Class<T> excelModelClass;
	private List<T> parsedObject;
	private List<FieldParameters> orderedFieldList;
	private Map<String, PropertyDescriptor> properties;

	public ExcelParser(boolean headerExtraction, Class<T> excelModelClass) {
		super();
		this.headerExtraction = headerExtraction;
		this.excelModelClass = excelModelClass;

		properties = new HashMap<String, PropertyDescriptor>();

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(excelModelClass, Object.class);
			for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
				String name = property.getName();
				properties.put(name, property);
			}
		} catch (IntrospectionException e) {
			// ignore and proceed to get fields direcly
		}

		parsedObject = new ArrayList<T>(100);
		if (!this.headerExtraction) {
			processFieldAccordingToPositions();
		}
	}

	public void processRow(List<String> row, int rowNo, String sheetName)
			throws UnexpectedException, IllegalAccessException {

		try {
			T excelModel = excelModelClass.newInstance();

			for (int i = 0; i < orderedFieldList.size(); i++) {

				FieldParameters fieldParameters = orderedFieldList.get(i);
				Field field = fieldParameters.getField();

				if (i >= row.size()) {

					if (fieldParameters.isRequired()) {
						throw new IllegalArgumentException(
								MessageFormat.format("Invalid data at row number: {0}, sheet: {1}", i, sheetName));
					}
				} else {

					String valueStr = row.get(i);
					if (fieldParameters.isTrim()) {
						valueStr = valueStr.trim();
					}
					if (fieldParameters.isLowerCase()) {
						valueStr = valueStr.toLowerCase();
					}

					boolean isNull = false;
					for (String nullStr : fieldParameters.getNulls()) {
						if (valueStr.equals(nullStr)) {
							valueStr = fieldParameters.getDefaultNullValue();
							isNull = true;
						}
					}

					if (!isNull) {

						Object value;
						if (field.getType().equals(DateTime.class)) {

							try {

								value = Conversion.convertDateTime(valueStr, fieldParameters.getDateTimeFormatter());
							} catch (UnsupportedOperationException e) {
								throw new IllegalArgumentException(MessageFormat.format(
										"Cannot convert date time for cell value: {0} at row number: {1}, sheet: {2}",
										row.get(i), rowNo, sheetName));
							} catch (IllegalArgumentException e) {
								throw new IllegalArgumentException(MessageFormat.format(
										"Cannot convert date time for cell value: {0} at row number: {1}, sheet: {2}",
										row.get(i), rowNo, sheetName));
							}

						} else {

							try {

								value = Conversion.toObject(field.getType(), row.get(i));
							} catch (IllegalArgumentException e) {
								throw new IllegalArgumentException(
										MessageFormat.format("Invalid cell value: {0} at row number: {1}, sheet: {2}",
												row.get(i), rowNo, sheetName));
							}
						}

						fieldParameters.write(excelModel, value);
					}
				}
			}
			parsedObject.add(excelModel);
		} catch (InstantiationException e) {
			throw new UnexpectedException("Unexpected error. Please try again later or contact admin.");
		} catch (IllegalAccessException e) {
			throw e;
		} catch (IndexOutOfBoundsException e) {
			throw new UnexpectedException("Unexpected error. Please try again later or contact admin.");
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (InvocationTargetException e) {
			throw new UnexpectedException("Unexpected error. Please try again later or contact admin.");
		}
	}

	public void processFieldAccordingToHeader(List<String> header, String sheetName) {

		if (header.size() == 0) {
			throw new IllegalArgumentException(
					MessageFormat.format("Invalid header at line 1 of sheet: {0}", sheetName));
		}

		Map<String, Integer> headerMap = new HashMap<String, Integer>(header.size());
		for (int i = 0; i < header.size(); i++) {
			headerMap.put(header.get(i).trim().toLowerCase(), i);
		}

		Field[] fields = excelModelClass.getDeclaredFields();
		FieldParameters[] orderedFieldArray = new FieldParameters[fields.length];

		int fieldCount = 0;
		for (Field field : fields) {

			if (field.isAnnotationPresent(Parsed.class)) {

				Parsed parsedAnnotation = field.getAnnotation(Parsed.class);

				String fieldName = parsedAnnotation.field();
				boolean fieldExists = headerMap.containsKey(fieldName.trim().toLowerCase());
				if (!fieldExists && parsedAnnotation.requird()) {

					throw new IllegalArgumentException(
							MessageFormat.format("In sheet{0}, Field {1} does not exists.", sheetName, fieldName));
				}

				if (fieldExists) {

					FieldParameters fieldParameters = this.processFieldParameters(field);
					orderedFieldArray[headerMap.get(fieldName)] = fieldParameters;
					fieldCount++;
				}
			}
		}

		if (fieldCount != header.size()) {

			throw new IllegalArgumentException(
					MessageFormat.format("In sheet{0}, Headers does not match the required fields", sheetName));
		}

		orderedFieldList = new ArrayList<>(orderedFieldArray.length);
		for (FieldParameters fieldParameters : orderedFieldArray) {
			if (fieldParameters != null) {
				orderedFieldList.add(fieldParameters);
			}
		}

	}

	private void processFieldAccordingToPositions() {

		Field[] fields = excelModelClass.getDeclaredFields();
		orderedFieldList = new ArrayList<FieldParameters>(fields.length);
		for (Field field : fields) {

			if (field.isAnnotationPresent(Parsed.class)) {

				FieldParameters fieldParameters = this.processFieldParameters(field);
				orderedFieldList.add(fieldParameters);
			}
		}
	}

	private FieldParameters processFieldParameters(Field field) {

		String[] nullString;
		if (field.isAnnotationPresent(NullString.class)) {

			NullString nullStringAnnotation = field.getAnnotation(NullString.class);
			nullString = nullStringAnnotation.nulls();
		} else {
			nullString = new String[0];
		}

		boolean lowerCase = false;
		if (field.isAnnotationPresent(LowerCase.class)) {
			lowerCase = true;
		}

		boolean trim = false;
		if (field.isAnnotationPresent(Trim.class)) {
			trim = true;
		}

		Parsed parsedAnnotation = field.getAnnotation(Parsed.class);
		String defaultNullValue = parsedAnnotation.defaultNullRead();
		if (defaultNullValue.equals("null")) {

			defaultNullValue = null;
		}

		String dateTimeFormatPatternStr = null;
		DateTimeFormatter dateTimeFormatter = null;
		if (field.getType().equals(DateTime.class)) {

			if (field.isAnnotationPresent(DateTimeFormatPattern.class)) {

				DateTimeFormatPattern dateTimeFormatPattern = field.getAnnotation(DateTimeFormatPattern.class);
				dateTimeFormatPatternStr = dateTimeFormatPattern.pattern();

				dateTimeFormatter = DateTimeFormat.forPattern(dateTimeFormatPatternStr);
			}
		}

		boolean required = parsedAnnotation.requird();

		FieldParameters fieldParameters = new FieldParameters(field, field.getType(), nullString, lowerCase, trim,
				defaultNullValue, dateTimeFormatPatternStr, dateTimeFormatter, required,
				properties.get(field.getName()));

		return fieldParameters;
	}

	public int parameterCount() {
		return orderedFieldList.size();
	}

	public List<T> getParsedObject() {
		return parsedObject;
	}

}
