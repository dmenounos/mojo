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
package mojo.dao.core.exec;

import java.util.HashMap;
import java.util.Map;

public class RepositoryRegistry {

	private static final Map<Class<?>, JpaRepository<?>> repositories;

	static {
		repositories = new HashMap<Class<?>, JpaRepository<?>>();
	}

	protected static <T> void add(Class<T> klass, JpaRepository<T> repository) {
		repositories.put(klass, repository);
	}

	@SuppressWarnings("unchecked")
	public static <T> JpaRepository<T> get(Class<T> klass) {
		return (JpaRepository<T>) repositories.get(klass);
	}
}
