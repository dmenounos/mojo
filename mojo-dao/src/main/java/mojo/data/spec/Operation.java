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

/**
 * Base query specification.
 * 
 * @param <E> the entity type
 */
public abstract class Operation<E> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class<? extends E> entityType;

	/**
	 * The entity class.
	 */
	public Class<? extends E> getEntityType() {
		return entityType;
	}

	public void setEntityType(Class<? extends E> entityType) {
		this.entityType = entityType;
	}
}
