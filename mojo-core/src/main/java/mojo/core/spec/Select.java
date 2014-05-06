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
package mojo.core.spec;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic retrieval query.
 */
public class Select<E> extends Operation<E> {

	private static final long serialVersionUID = 1L;

	private boolean distinct;
	private List<Join> joins;
	private Filter filter;
	private String order;
	private int offset;
	private int limit;

	public Select() {
	}

	public Select(Filter filter) {
		setFilter(filter);
	}

	public Select(Class<? extends E> entityType) {
		setEntityType(entityType);
	}

	public Select(Class<? extends E> entityType, Filter filter) {
		setEntityType(entityType);
		setFilter(filter);
	}

	/**
	 * Specifies whether duplicates should be returned.
	 */
	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * The relations that should be initialized.
	 */
	public List<Join> getJoins() {
		if (joins == null) {
			joins = new ArrayList<Join>();
		}

		return joins;
	}

	public void setJoins(List<Join> joins) {
		this.joins = joins;
	}

	/**
	 * Filter specification bean.
	 */
	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	/**
	 * The order-by property names and direction.
	 */
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * The page starting index.
	 */
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * The page size.
	 */
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	// fluent configuration ...

	public Select<E> from(Class<? extends E> type) {
		setEntityType(type);
		return this;
	}

	public Select<E> distinct() {
		setDistinct(true);
		return this;
	}

	public Select<E> join(String property) {
		Join join = new Join("left", property);
		getJoins().add(join);
		return this;
	}

	public Select<E> where(String criteria) {
		BySQL bysql = new BySQL(criteria);
		setFilter(bysql);
		return this;
	}

	public Select<E> filter(Filter filter) {
		setFilter(filter);
		return this;
	}

	public Select<E> order(String order) {
		setOrder(order);
		return this;
	}

	public Select<E> page(int offset, int limit) {
		setOffset(offset);
		setLimit(limit);
		return this;
	}
}
