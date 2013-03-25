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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Find by matching a value against a number of properties.
 */
public class ByText implements Filter {

	private static final long serialVersionUID = 1L;

	public static final int STARTS = 1;
	public static final int ENDS = 2;

	private int mode;
	private String value;
	private Set<String> properties;

	public ByText() {
	}

	public ByText(String value, String... properties) {
		setValue(value);
		Collections.addAll(getProperties(), properties);
	}

	/**
	 * Match mode:<br>
	 * 0) has anywhere<br>
	 * 1) starts-with<br>
	 * 2) ends-with
	 */
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * The value that will be matched.
	 */
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * The properties that will be used in search.
	 */
	public Set<String> getProperties() {
		if (properties == null) {
			properties = new HashSet<String>();
		}

		return properties;
	}

	public void setProperties(Set<String> properties) {
		this.properties = properties;
	}
}
