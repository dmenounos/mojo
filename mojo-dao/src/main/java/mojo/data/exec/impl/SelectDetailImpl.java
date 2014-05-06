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

import java.util.Map;

import mojo.dao.core.exec.JpaSubRepository;
import mojo.dao.core.spec.Select;
import mojo.dao.core.spec.SelectDetail;
import mojo.dao.core.util.Properties;

public class SelectDetailImpl<E> extends SelectImpl<E> {

	protected static final String MASTER_ALIAS = "m";

	@Override
	public Class<?> getType() {
		return SelectDetail.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void from(Select<E> select, StringBuilder sb) {
		SelectDetail<E> spec = (SelectDetail<E>) select;

		if (spec.getMasterId() != null) {
			JpaSubRepository<E> repository = (JpaSubRepository<E>) getRepository();

			if (!repository.isInverseLink()) {
				sb.append(spec.getEntityType().getName() + " " + ALIAS);
				sb.append(" join " + ALIAS + "." + repository.getPropertyName() + " " + MASTER_ALIAS);
			}
			else {
				sb.append(repository.getMasterType().getName() + " " + MASTER_ALIAS);
				sb.append(" join " + MASTER_ALIAS + "." + repository.getPropertyName() + " " + ALIAS);
			}
		}
		else {
			super.from(spec, sb);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void criteria(Select<E> spec, StringBuilder cb, Map<String, Object> params) {
		SelectDetail<E> select = (SelectDetail<E>) spec;

		if (select.getMasterId() != null) {
			JpaSubRepository<E> repository = (JpaSubRepository<E>) getRepository();
			String masterIdProperty = Properties.idProperty(repository.getMasterType());
			cb.append(MASTER_ALIAS + "." + masterIdProperty + " = :masterId");
			params.put("masterId", select.getMasterId());
		}

		super.criteria(spec, cb, params);
	}
}
