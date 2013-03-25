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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import mojo.dao.core.jpa.JpaFilterExecutor;
import mojo.dao.core.spec.ByText;
import mojo.dao.core.spec.Filter;

public class ByTextImpl implements JpaFilterExecutor {

	@Override
	public Class<?> getType() {
		return ByText.class;
	}

	@Override
	public void criteria(Filter spec, StringBuilder sb, Map<String, Object> params) {
		ByText by = (ByText) spec;
		String value = by.getValue();

		if (value != null) {
			Set<String> properties = by.getProperties();
			boolean starts = (by.getMode() & ByText.STARTS) > 0;
			boolean ends = (by.getMode() & ByText.ENDS) > 0;
			value = (!starts ? "%" : "") + value + (!ends ? "%" : "");
			sb.append("(");

			for (Iterator<String> it = properties.iterator(); it.hasNext();) {
				String property = it.next();
				String parameter = property.replace('.', '_');
				sb.append(ALIAS + ".").append(property).append(" like :").append(parameter);

				if (it.hasNext()) {
					sb.append(" or ");
				}

				params.put(parameter, value);
			}

			sb.append(")");
		}
	}
}
