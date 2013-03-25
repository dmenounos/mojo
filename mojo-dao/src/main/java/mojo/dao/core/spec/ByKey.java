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

/**
 * Find by key.
 */
public class ByKey implements Filter {

	private static final long serialVersionUID = 1L;

	private Object key;
	private String property;
	private boolean not;

	public ByKey() {
	}

	public ByKey(Object key) {
		setKey(key);
	}

	/**
	 * The key that will be matched.
	 */
	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	/**
	 * The relation property (optional).
	 */
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * Invert the condition (optional).
	 */
	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}

	// fluent configuration ...

	public ByKey key(Object key) {
		setKey(key);
		return this;
	}

	public ByKey property(String property) {
		setProperty(property);
		return this;
	}
}
