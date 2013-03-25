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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Batch<E> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Insert<E>> inserts;
	private List<Update<E>> updates;
	private List<Delete<E>> deletes;

	public List<Insert<E>> getInserts() {
		if (inserts == null) {
			inserts = new ArrayList<Insert<E>>();
		}

		return inserts;
	}

	public void setInserts(List<Insert<E>> inserts) {
		this.inserts = inserts;
	}

	public List<Update<E>> getUpdates() {
		if (updates == null) {
			updates = new ArrayList<Update<E>>();
		}

		return updates;
	}

	public void setUpdates(List<Update<E>> updates) {
		this.updates = updates;
	}

	public List<Delete<E>> getDeletes() {
		if (deletes == null) {
			deletes = new ArrayList<Delete<E>>();
		}

		return deletes;
	}

	public void setDeletes(List<Delete<E>> deletes) {
		this.deletes = deletes;
	}

	// fluent configuration ...

	public Batch<E> insert(Insert<E>... inserts) {
		Collections.addAll(getInserts(), inserts);
		return this;
	}

	public Batch<E> update(Update<E>... updates) {
		Collections.addAll(getUpdates(), updates);
		return this;
	}

	public Batch<E> delete(Delete<E>... deletes) {
		Collections.addAll(getDeletes(), deletes);
		return this;
	}
}
