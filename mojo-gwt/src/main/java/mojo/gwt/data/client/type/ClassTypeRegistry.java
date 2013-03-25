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
package mojo.gwt.data.client.type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassTypeRegistry {

	private static final Map<Class<?>, ClassType<?>> types;
	private static final Set<String> scalars;

	static {
		types = new HashMap<Class<?>, ClassType<?>>();

		scalars = new HashSet<String>();
		scalars.add("java.lang.String");
		scalars.add("java.lang.Integer");
		scalars.add("java.lang.Long");
		scalars.add("java.lang.Float");
		scalars.add("java.lang.Double");
		scalars.add("java.lang.Boolean");
		scalars.add("java.util.Date");
	}

	public static boolean isScalar(String className) {
		return scalars.contains(className);
	}

	public static <T> void add(ClassType<T> classType) {
		types.put(classType.getKlass(), classType);
	}

	@SuppressWarnings("unchecked")
	public static <T> ClassType<T> get(Class<T> klass) {
		return (ClassType<T>) types.get(klass);
	}
}
