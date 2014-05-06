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
package mojo.dao.core.spec;

import java.util.HashMap;
import java.util.Map;

/**
 * Find by raw SQL criteria.
 */
public class BySQL implements Filter {

	private static final long serialVersionUID = 1L;

	private String criteria;
	private Map<String, Object> params;

	public BySQL() {
	}

	public BySQL(String criteria) {
		setCriteria(criteria);
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public Map<String, Object> getParams() {
		if (params == null) {
			params = new HashMap<String, Object>();
		}

		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	// fluent configuration ...

	public BySQL param(String key, Object value) {
		getParams().put(key, value);
		return this;
	}
}
