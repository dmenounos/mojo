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
package mojo.dao.core.jpa.exec;

import java.util.Map;

import mojo.dao.core.jpa.JpaFilterExecutor;
import mojo.dao.core.spec.BySQL;
import mojo.dao.core.spec.Filter;

public class BySQLImpl implements JpaFilterExecutor {

	@Override
	public Class<?> getType() {
		return BySQL.class;
	}

	@Override
	public void criteria(Filter spec, StringBuilder sb, Map<String, Object> params) {
		BySQL by = (BySQL) spec;
		String criteria = by.getCriteria();

		if (criteria != null) {
			sb.append(criteria);
			params.putAll(by.getParams());
		}
	}
}
