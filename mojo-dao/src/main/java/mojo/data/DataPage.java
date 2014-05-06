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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data container that holds a page of entities.
 */
public class DataPage<E> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<E> data;
	private Long total;

	/**
	 * Returns the single contained entity or null.
	 * 
	 * @throws DataException when there are more than a single entity
	 */
	public E unique() {
		switch (getData().size()) {
		case 0:
			return null;
		case 1:
			return getData().get(0);
		default:
			throw new DataException("Non unique data set; Found " + getData().size());
		}
	}

	/**
	 * The contained entities.
	 */
	public List<E> getData() {
		if (data == null) {
			data = new ArrayList<E>();
		}

		return data;
	}

	public void setData(List<E> data) {
		this.data = data;
	}

	/**
	 * The total number of entities in the data store.
	 */
	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
