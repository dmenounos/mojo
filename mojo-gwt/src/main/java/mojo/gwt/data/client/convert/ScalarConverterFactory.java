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
package mojo.gwt.data.client.convert;

import java.util.HashMap;
import java.util.Map;

public class ScalarConverterFactory<T> {

	private Map<String, ScalarConverter<T>> converters;

	/**
	 * Constructs the factory with a default converter.
	 */
	public ScalarConverterFactory(ScalarConverter<T> converter) {
		converters = new HashMap<String, ScalarConverter<T>>();
		converters.put(null, converter);
	}

	/**
	 * Provides a converter for a pattern.
	 * 
	 * If a converter does not exist for that pattern it will be created and
	 * stored for efficient later use. Null pattern is the default converter.
	 */
	public ScalarConverter<T> get(String pattern) {
		ScalarConverter<T> converter = converters.get(pattern);

		if (converter == null) {
			converter = create(pattern);
			converters.put(pattern, converter);
		}

		return converter;
	}

	/**
	 * Creates a converter for a given pattern. The default implementation
	 * returns the default converter for any pattern.
	 * 
	 * Override this to create custom factories.
	 */
	protected ScalarConverter<T> create(String pattern) {
		return converters.get(null);
	}
}
