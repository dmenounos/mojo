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

import javax.persistence.EntityManager;

import mojo.dao.core.jpa.JpaQueryExecutor;
import mojo.dao.core.spec.Delete;
import mojo.dao.core.spec.Operation;

public class DeleteImpl<E> extends JpaQueryExecutor<E, E> {

	@Override
	public Class<?> getType() {
		return Delete.class;
	}

	@Override
	public E execute(Operation<E> spec) {
		logger.debug("--> execute()");
		Delete<E> delete = (Delete<E>) spec;
		EntityManager entityManager = getRepository().getEntityManager();
		E entity = entityManager.find(delete.getEntityType(), delete.getId());
		entityManager.remove(entity);
		entityManager.flush();
		return entity;
	}
}
