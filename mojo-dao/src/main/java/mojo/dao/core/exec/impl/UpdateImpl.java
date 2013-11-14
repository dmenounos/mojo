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
package mojo.dao.core.exec.impl;

import javax.persistence.EntityManager;

import mojo.dao.core.exec.JpaQueryExecutor;
import mojo.dao.core.spec.Operation;
import mojo.dao.core.spec.Update;

public class UpdateImpl<E> extends JpaQueryExecutor<E, E> {

	@Override
	public Class<?> getType() {
		return Update.class;
	}

	@Override
	public E execute(Operation<E> spec) {
		logger.debug("--> execute()");
		E entity = ((Update<E>) spec).getEntity();
		EntityManager entityManager = getRepository().getEntityManager();
		entity = entityManager.merge(entity);
		entityManager.flush();
		return entity;
	}
}
