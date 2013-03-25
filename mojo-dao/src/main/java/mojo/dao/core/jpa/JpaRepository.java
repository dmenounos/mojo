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

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mojo.dao.core.DataException;
import mojo.dao.core.DataPage;
import mojo.dao.core.Repository;
import mojo.dao.core.jpa.exec.ByKeyImpl;
import mojo.dao.core.jpa.exec.BySQLImpl;
import mojo.dao.core.jpa.exec.ByTextImpl;
import mojo.dao.core.jpa.exec.DeleteImpl;
import mojo.dao.core.jpa.exec.InsertImpl;
import mojo.dao.core.jpa.exec.SelectImpl;
import mojo.dao.core.jpa.exec.UpdateImpl;
import mojo.dao.core.spec.Delete;
import mojo.dao.core.spec.Insert;
import mojo.dao.core.spec.Operation;
import mojo.dao.core.spec.Select;
import mojo.dao.core.spec.Update;

public class JpaRepository<E> implements Repository<E> {

	@PersistenceContext
	private EntityManager entityManager;

	private Class<? extends E> entityType;
	private Map<Class<?>, JpaExecutor> executors;

	public JpaRepository() {
		executors = new HashMap<Class<?>, JpaExecutor>();

		addExecutor(new ByKeyImpl());
		addExecutor(new BySQLImpl());
		addExecutor(new ByTextImpl());
		addExecutor(new SelectImpl<E>());
		addExecutor(new InsertImpl<E>());
		addExecutor(new UpdateImpl<E>());
		addExecutor(new DeleteImpl<E>());
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Configuration property (optional).<br />
	 * Overrides the same Operation property before execution.
	 */
	public Class<? extends E> getEntityType() {
		return entityType;
	}

	public void setEntityType(Class<? extends E> entityType) {
		this.entityType = entityType;
	}

	@SuppressWarnings("unchecked")
	public void addExecutor(JpaExecutor executor) {
		if (executor instanceof JpaQueryExecutor) {
			((JpaQueryExecutor<E, ?>) executor).setRepository(this);
		}

		executors.put(executor.getType(), executor);
	}

	public JpaExecutor getExecutor(Class<?> type) {
		JpaExecutor executor = executors.get(type);

		if (executor == null) {
			throw new DataException("Non-registered specification: " + type.getName());
		}

		return executor;
	}

	@SuppressWarnings("unchecked")
	public <R> R execute(Operation<E> op) {
		if (getEntityType() != null) {
			// override operation entityType
			op.setEntityType(getEntityType());
		}
		else if (op.getEntityType() == null) {
			StringBuilder sb = new StringBuilder("Null 'entityType'; ");
			sb.append("either the repository or the query should have 'entityType'.");
			throw new DataException(sb.toString());
		}

		try {
			Class<?> specType = op.getClass();
			JpaExecutor exec = getExecutor(specType);
			return ((JpaQueryExecutor<E, R>) exec).execute(op);
		}
		catch (DataException e) {
			throw e;
		}
		catch (Exception e) {
			throw new DataException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public DataPage<E> select(Select<E> select) {
		return (DataPage<E>) execute(select);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E insert(Insert<E> insert) {
		return (E) execute(insert);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E update(Update<E> update) {
		return (E) execute(update);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E delete(Delete<E> delete) {
		return (E) execute(delete);
	}
}
