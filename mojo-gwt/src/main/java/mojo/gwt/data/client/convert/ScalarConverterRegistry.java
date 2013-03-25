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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mojo.gwt.data.client.convert.base.BooleanConverter;
import mojo.gwt.data.client.convert.base.DateConverterFactory;
import mojo.gwt.data.client.convert.base.DoubleConverter;
import mojo.gwt.data.client.convert.base.FloatConverter;
import mojo.gwt.data.client.convert.base.IntegerConverter;
import mojo.gwt.data.client.convert.base.LongConverter;
import mojo.gwt.data.client.convert.base.StringConverter;

public class ScalarConverterRegistry {

	private static Map<Class<?>, ScalarConverterFactory<?>> factories;

	static {
		factories = new HashMap<Class<?>, ScalarConverterFactory<?>>();

		// @formatter:off
		add(String.class,  new StringConverter());
		add(Integer.class, new IntegerConverter());
		add(Long.class,    new LongConverter());
		add(Float.class,   new FloatConverter());
		add(Double.class,  new DoubleConverter());
		add(Boolean.class, new BooleanConverter());
		add(Date.class,    new DateConverterFactory());
		// @formatter:on
	}

	protected static <T> void add(Class<T> klass, ScalarConverterFactory<T> factory) {
		factories.put(klass, factory);
	}

	protected static <T> void add(Class<T> klass, ScalarConverter<T> converter) {
		factories.put(klass, new ScalarConverterFactory<T>(converter));
	}

	@SuppressWarnings("unchecked")
	public static <T> ScalarConverter<T> get(Class<T> klass, String pattern) {
		ScalarConverterFactory<?> factory = factories.get(klass);
		return (ScalarConverter<T>) factory.get(pattern);
	}
}
