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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.Query;

import mojo.dao.core.DataPage;
import mojo.dao.core.exec.JpaExecutor;
import mojo.dao.core.exec.JpaFilterExecutor;
import mojo.dao.core.exec.JpaQueryExecutor;
import mojo.dao.core.spec.Join;
import mojo.dao.core.spec.Operation;
import mojo.dao.core.spec.Select;
import mojo.dao.core.util.Properties;

public class SelectImpl<E> extends JpaQueryExecutor<E, DataPage<E>> {

	private static final Pattern orderPattern = Pattern.compile("(^|,) *");

	@Override
	public Class<?> getType() {
		return Select.class;
	}

	@Override
	public DataPage<E> execute(Operation<E> spec) {
		// logger.debug("--> execute()");
		Select<E> select = (Select<E>) spec;
		DataPage<E> page = new DataPage<E>();
		page.setData(fetchList(select));

		if (select.getLimit() > 0) {
			page.setTotal(countTotal(select));
		}

		return page;
	}

	@SuppressWarnings("unchecked")
	private List<E> fetchList(Select<E> spec) {
		// logger.debug("--> fetchList()");
		StringBuilder sb = new StringBuilder("select ");

		if (spec.isDistinct()) {
			sb.append("distinct ");
		}

		sb.append(ALIAS).append(" from ");
		from(spec, sb);

		for (Join join : spec.getJoins()) {
			if (join.getType() != null) {
				sb.append(" " + join.getType());
			}

			sb.append(" join fetch " + ALIAS + "." + join.getProperty());
		}

		Map<String, Object> params = where(spec, sb);

		String order = spec.getOrder();

		if (order != null && !order.isEmpty()) {
			order = orderPattern.matcher(order).replaceAll("$0" + ALIAS + ".");
			sb.append(" order by ").append(order);
		}
		else if (spec.getLimit() > 0) {
			String idProperty = Properties.idProperty(spec.getEntityType());
			sb.append(" order by " + ALIAS + "." + idProperty);
		}

		logger.debug(sb.toString());
		Query query = getRepository().getEntityManager().createQuery(sb.toString());

		for (Map.Entry<String, Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}

		if (spec.getOffset() > 0) {
			query.setFirstResult(spec.getOffset());
		}

		if (spec.getLimit() > 0) {
			query.setMaxResults(spec.getLimit());
		}

		return query.getResultList();
	}

	private Long countTotal(Select<E> spec) {
		// logger.debug("--> countTotal()");
		StringBuilder sb = new StringBuilder("select count(");

		if (spec.isDistinct()) {
			sb.append("distinct ");
		}

		sb.append(ALIAS + ") from ");
		from(spec, sb);

		Map<String, Object> params = where(spec, sb);

		logger.debug(sb.toString());
		Query query = getRepository().getEntityManager().createQuery(sb.toString());

		for (Map.Entry<String, Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}

		return (Long) query.getSingleResult();
	}

	protected void from(Select<E> spec, StringBuilder sb) {
		sb.append(spec.getEntityType().getName()).append(" ").append(ALIAS);
	}

	protected Map<String, Object> where(Select<E> spec, StringBuilder sb) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder cb = new StringBuilder();
		criteria(spec, cb, params);

		if (cb.length() > 0) {
			sb.append(" where ").append(cb);
		}

		return params;
	}

	protected void criteria(Select<E> spec, StringBuilder cb, Map<String, Object> params) {
		if (spec.getFilter() != null) {
			if (cb.length() > 0) {
				cb.append(" and ");
			}

			JpaExecutor executor = getRepository().getExecutor(spec.getFilter().getClass());
			((JpaFilterExecutor) executor).criteria(spec.getFilter(), cb, params);
		}
	}
}
