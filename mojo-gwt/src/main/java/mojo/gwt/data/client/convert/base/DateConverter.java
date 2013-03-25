/*
 * Copyright (C) 2010 Dimitrios Menounos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mojo.gwt.data.client.convert.base;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

import mojo.gwt.data.client.convert.ScalarConverter;

/**
 * Convenience converter for dates and times.
 * 
 * Compatible with XStream by default.
 * 
 * Ignores nulls and empty strings.
 */
public class DateConverter implements ScalarConverter<Date> {

	/**
	 * XStream default pattern, e.g. 2010-05-15 02:10:08.960 EEST
	 * 
	 * @see com.thoughtworks.xstream.converters.basic.DateConverter
	 */
	public static final String PATTERN = "yyyy-MM-dd HH:mm:ss.S z";

	private DateTimeFormat format;

	/**
	 * Create a converter with the default pattern.
	 */
	public DateConverter() {
		this(PATTERN);
	}

	/**
	 * Create a converter with a custom pattern.
	 */
	public DateConverter(String pattern) {
		if (pattern == null || pattern.isEmpty()) {
			pattern = PATTERN;
		}

		format = DateTimeFormat.getFormat(pattern);
	}

	@Override
	public String format(Date date) {
		if (date != null) {
			return format.format(date);
		}

		return null;
	}

	@Override
	public Date parse(String date) {
		if (date != null && !date.isEmpty()) {
			return format.parse(date);
		}

		return null;
	}
}
