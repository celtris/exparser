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
package com.celtris.exparse.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Pratick Chokhani <pratick@celtris.com>
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(value = { ElementType.FIELD })
public @interface Parsed {
	/**
	 * The field name in a parsed record
	 * 
	 * @return the field name (optional if the index is provided)
	 */
	String field() default "";

	boolean requird() default false;

	/**
	 * The default value to assign to this field in the parsed value is null
	 * <p>
	 * The String literal "null" will be interpreted as a regular null.
	 * <p>
	 * Use "'null"' if you want the default value to be the string "null"
	 *
	 * <p>
	 * this value will have different effects depending on the field type:
	 * <ul>
	 * <li>on boolean and Boolean fields: if the null value contains a String,
	 * the result of Boolean.valueOf(defaultNullRead()) will assigned to the
	 * field.
	 * <li>on char and Character fields: if the null value contains a String,
	 * the result of defaultNullRead().charAt(0) will assigned to the field. An
	 * exception will be thrown if the length of this String is different than 1
	 * </ul>
	 *
	 * @return the default String to return when the parsed value is null
	 */
	String defaultNullRead() default "null";
}
