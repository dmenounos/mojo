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
package mojo.dao.core;

import java.util.LinkedList;
import java.util.List;

import mojo.dao.core.spec.Batch;
import mojo.dao.core.spec.ByKey;
import mojo.dao.core.spec.Delete;
import mojo.dao.core.spec.Insert;
import mojo.dao.core.spec.Select;
import mojo.dao.core.spec.Update;

/**
 * Generic data access service.
 * 
 * @param <E> the entity type
 */
public class DataService<E> {

	private Repository<E> repository;
	private Validation<E> validation;

	public Repository<E> getRepository() {
		return repository;
	}

	public void setRepository(Repository<E> repository) {
		this.repository = repository;
	}

	public Validation<E> getValidation() {
		return validation;
	}

	public void setValidation(Validation<E> validation) {
		this.validation = validation;
	}

	/**
	 * Find an entity by id.
	 */
	public E findById(Object id) {
		Select<E> findById = new Select<E>(new ByKey(id));
		return repository.select(findById).unique();
	}

	/**
	 * Lookup for entities.
	 */
	public DataPage<E> select(Select<E> select) {
		return repository.select(select);
	}

	/**
	 * Create an entity.
	 */
	public E insert(E entity) {
		validate(entity);
		beforeInsert(entity);
		return repository.insert(new Insert<E>(entity));
	}

	/**
	 * Update an entity.
	 */
	public E update(E entity) {
		validate(entity);
		beforeUpdate(entity);
		return repository.update(new Update<E>(entity));
	}

	/**
	 * Delete an entity.
	 */
	public void delete(Object id) {
		beforeDelete(id);
		repository.delete(new Delete<E>(id));
	}

	/**
	 * Create, Update, Delete multiple entities.
	 */
	public void persist(Batch<E> batch) {
		for (Insert<E> spec : batch.getInserts()) {
			validate(spec.getEntity());
		}

		for (Update<E> spec : batch.getUpdates()) {
			validate(spec.getEntity());
		}

		for (Insert<E> spec : batch.getInserts()) {
			beforeInsert(spec.getEntity());
			repository.insert(spec);
		}

		for (Update<E> spec : batch.getUpdates()) {
			beforeUpdate(spec.getEntity());
			repository.update(spec);
		}

		for (Delete<E> spec : batch.getDeletes()) {
			beforeDelete(spec.getId());
			repository.delete(spec);
		}
	}

	/**
	 * @throws ValidationException
	 */
	protected void validate(E entity) {
		if (validation == null) {
			return;
		}

		List<String> errors = new LinkedList<String>();
		validation.validate(entity, errors);

		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
	}

	/**
	 * @throws DataException
	 */
	protected void beforeInsert(E entity) {
	}

	/**
	 * @throws DataException
	 */
	protected void beforeUpdate(E entity) {
	}

	/**
	 * @throws DataException
	 */
	protected void beforeDelete(Object id) {
	}
}
