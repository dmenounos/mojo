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
 * Delete an entity.
 */
public class Delete<E> extends Operation<E> {

	private static final long serialVersionUID = 1L;

	private Object id;

	public Delete() {
	}

	public Delete(Object id) {
		setId(id);
	}

	public Delete(Class<? extends E> entityType, Object id) {
		setEntityType(entityType);
		setId(id);
	}

	/**
	 * The id of the entity to delete.
	 */
	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}
}
