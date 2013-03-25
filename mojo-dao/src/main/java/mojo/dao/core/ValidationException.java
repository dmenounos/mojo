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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<String> errors;

	public ValidationException(List<String> errors) {
		this.errors = errors;
	}

	public List<String> getErrors() {
		if (errors == null) {
			errors = new ArrayList<String>();
		}

		return errors;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();

		for (Iterator<String> it = getErrors().iterator(); it.hasNext();) {
			sb.append(it.next());

			if (it.hasNext()) {
				sb.append('\n');
			}
		}

		return sb.toString();
	}
}
