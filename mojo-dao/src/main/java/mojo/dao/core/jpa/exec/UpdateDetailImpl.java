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
package mojo.dao.core.jpa.exec;

import java.util.List;

import javax.persistence.EntityManager;

import mojo.dao.core.jpa.JpaSubRepository;
import mojo.dao.core.spec.Operation;
import mojo.dao.core.spec.UpdateDetail;
import mojo.dao.core.util.Properties;

public class UpdateDetailImpl<E> extends UpdateImpl<E> {

	@Override
	public Class<?> getType() {
		return UpdateDetail.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E execute(Operation<E> spec) {
		logger.debug("--> execute()");
		UpdateDetail<E> update = (UpdateDetail<E>) spec;

		JpaSubRepository<E> repository = (JpaSubRepository<E>) getRepository();
		EntityManager entityManager = repository.getEntityManager();

		String idProperty = Properties.idProperty(update.getEntity().getClass());
		Object id = Properties.id(update.getEntity().getClass(), update.getEntity());

		String linkProperty = repository.getPropertyName();
		List<?> masters = null;

		if (repository.isInverseLink()) {
			String masterType = repository.getMasterType().getName();
			logger.debug("Searching parent for one-to-many relation: " + masterType + " - " + linkProperty);
			String masterQuery = "select p from " + masterType + " p join p." + linkProperty + " e where e." + idProperty + " = :id";
			masters = entityManager.createQuery(masterQuery).setParameter("id", id).getResultList();
		}
		else {
			String entityType = update.getEntity().getClass().getName();
			logger.debug("Searching parent for many-to-one relation: " + entityType + " - " + linkProperty);
			String masterQuery = "select p from " + entityType + " e join e." + linkProperty + " p where e." + idProperty + " = :id";
			masters = entityManager.createQuery(masterQuery).setParameter("id", id).getResultList();
		}

		for (Object master : masters) {
			Object oldParentId = Properties.id(repository.getMasterType(), master);
			logger.debug("Old master id: " + oldParentId + ", New master id: " + update.getMasterId());

			if (!oldParentId.equals(update.getMasterId())) {
				E managedEntity = (E) entityManager.find(update.getEntity().getClass(), id);
				repository.unlink(master, managedEntity);
			}

			entityManager.flush();
		}

		if (update.getMasterId() != null) {
			Object master = entityManager.find(repository.getMasterType(), update.getMasterId());
			repository.link(master, update.getEntity());
		}

		return super.execute(update);
	}
}
