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
package mojo.view.util;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * PropertyUtils thin wrapper.
 */
public class BeanUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object obj, String prop) {
		try {
			return (T) PropertyUtils.getProperty(obj, prop);
		}
		catch (Exception e) {
			return null;
		}
	}

	public static void setProperty(Object obj, String prop, Object val) {
		try {
			PropertyUtils.setProperty(obj, prop, val);
		}
		catch (Exception e) {
			// do nothing
		}
	}

	public static void copyProperty(Object source, Object target, String property) {
		copyProperty(source, target, property, property);
	}

	public static void copyProperty(Object source, Object target, String sourceProperty, String targetProperty) {
		Object value = getProperty(source, sourceProperty);
		setProperty(target, targetProperty, value);
	}
}
