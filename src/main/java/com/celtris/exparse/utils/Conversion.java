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
package com.celtris.exparse.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author Pratick Chokhani <pratick@celtris.com>
 *
 */
public class Conversion {
	public static Object toObject(Class<?> clazz, String value) {
		if (int.class == clazz || Integer.class == clazz) {
			return (int) Double.parseDouble(value);
		} else if (double.class == clazz || Double.class == clazz) {
			return Double.parseDouble(value);
		} else if (float.class == clazz || Float.class == clazz) {
			return (float) Double.parseDouble(value);
		} else if (boolean.class == clazz || Boolean.class == clazz) {
			return Boolean.parseBoolean(value);
		} else if (long.class == clazz || Long.class == clazz) {
			return (long) Double.parseDouble(value);
		} else if (byte.class == clazz || Byte.class == clazz) {
			return Byte.parseByte(value);
		} else if (short.class == clazz || Short.class == clazz) {
			return Short.parseShort(value);
		}
		return value;
	}

	public static DateTime convertDateTime(String value, DateTimeFormatter dateTimeFormatter) {

		if (dateTimeFormatter == null) {
			try {

				return DateTime.parse(value);
			} catch (UnsupportedOperationException e) {
				throw new IllegalArgumentException();
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException();
			}
		} else {

			try {

				return dateTimeFormatter.parseDateTime(value);
			} catch (UnsupportedOperationException e) {
				throw new IllegalArgumentException();
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException();
			}
		}
	}
}
