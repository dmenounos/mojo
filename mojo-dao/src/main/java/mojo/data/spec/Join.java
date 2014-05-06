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

import java.io.Serializable;

public class Join implements Serializable {

	private static final long serialVersionUID = 1L;

	private String type;
	private String property;

	public Join() {
	}

	public Join(String property) {
		setProperty(property);
	}

	public Join(String type, String property) {
		setType(type);
		setProperty(property);
	}

	/**
	 * Join type: left or right.
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Property name to join.
	 */
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
