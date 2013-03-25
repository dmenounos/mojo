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
package mojo.dao.core;

import java.util.List;

public abstract class Validation<E> {

	protected abstract void validate(E entity, List<String> errors);

	/**
	 * Helper method.
	 */
	protected boolean checkNull(Object obj, List<String> errors, String error) {
		if (obj == null) {
			errors.add(error);
			return true;
		}

		return false;
	}

	/**
	 * Helper method.
	 */
	protected boolean checkEmpty(String str, List<String> errors, String error) {
		if (str == null || str.trim().equals("")) {
			errors.add(error);
			return true;
		}

		return false;
	}
}
