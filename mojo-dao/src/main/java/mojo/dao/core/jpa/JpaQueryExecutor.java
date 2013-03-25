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
package mojo.dao.core.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mojo.dao.core.spec.Operation;

public abstract class JpaQueryExecutor<E, R> implements JpaExecutor {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private JpaRepository<E> repository;

	public JpaRepository<E> getRepository() {
		return repository;
	}

	public void setRepository(JpaRepository<E> repository) {
		this.repository = repository;
	}

	public abstract R execute(Operation<E> spec);
}
