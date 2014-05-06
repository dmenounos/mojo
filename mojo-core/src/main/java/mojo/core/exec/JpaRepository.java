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
package mojo.core.exec;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mojo.core.DataException;
import mojo.core.DataPage;
import mojo.core.Repository;
import mojo.core.exec.impl.ByKeyImpl;
import mojo.core.exec.impl.BySQLImpl;
import mojo.core.exec.impl.ByTextImpl;
import mojo.core.exec.impl.DeleteImpl;
import mojo.core.exec.impl.InsertImpl;
import mojo.core.exec.impl.SelectImpl;
import mojo.core.exec.impl.UpdateImpl;
import mojo.core.spec.Delete;
import mojo.core.spec.Insert;
import mojo.core.spec.Operation;
import mojo.core.spec.Select;
import mojo.core.spec.Update;

public class JpaRepository<E> implements Repository<E> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

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

	public void addExecutor(JpaExecutor executor) {
		executor.setRepository(this);
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
			JpaExecutor exec = getExecutor(op.getClass());
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
