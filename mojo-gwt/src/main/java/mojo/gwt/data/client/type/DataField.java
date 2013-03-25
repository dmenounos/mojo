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
import java.util.Map;

public class DataField implements ClassField {

	private String name;
	private Class<?> klass;

	/* Field Annotations */
	private Map<String, Object> params;

	public DataField(String name, Class<?> klass) {
		setName(name);
		setKlass(klass);
	}

	@Override
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		assert name != null;
		this.name = name;
	}

	@Override
	public Class<?> getKlass() {
		return klass;
	}

	protected void setKlass(Class<?> klass) {
		assert klass != null;
		this.klass = klass;
	}

	@Override
	public Object getParam(String key, Object defaultValue) {
		if (params != null && params.containsKey(key)) {
			return params.get(key);
		}

		return defaultValue;
	}

	@Override
	public ClassField setParam(String key, Object value) {
		if (params == null) {
			params = new HashMap<String, Object>();
		}

		params.put(key, value);
		return this;
	}
}
