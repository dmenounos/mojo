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
 * Update an entity and associate it to a parent.
 */
public class UpdateDetail<E> extends Update<E> {

	private static final long serialVersionUID = 1L;

	private Object masterId;

	public UpdateDetail() {
	}

	public UpdateDetail(Object masterId, E entity) {
		super(entity);
		setMasterId(masterId);
	}

	/**
	 * The parent to associate with the entity.
	 */
	public Object getMasterId() {
		return masterId;
	}

	public void setMasterId(Object masterId) {
		this.masterId = masterId;
	}
}
