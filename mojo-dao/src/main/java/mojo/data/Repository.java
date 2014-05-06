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

import mojo.dao.core.spec.Delete;
import mojo.dao.core.spec.Insert;
import mojo.dao.core.spec.Select;
import mojo.dao.core.spec.Update;

/**
 * Generic data access repository.
 * 
 * @param <E> the entity type
 */
public interface Repository<E> {

	/**
	 * Retrieve entities dynamically.
	 * 
	 * @throws DataException
	 */
	DataPage<E> select(Select<E> select);

	/**
	 * Create an entity.
	 * 
	 * @throws DataException
	 */
	E insert(Insert<E> insert);

	/**
	 * Update an entity.
	 * 
	 * @throws DataException
	 */
	E update(Update<E> update);

	/**
	 * Delete an entity.
	 * 
	 * @throws DataException
	 */
	E delete(Delete<E> delete);
}
