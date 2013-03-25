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

import java.lang.reflect.Method;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mojo.dao.core.jpa.exec.InsertDetailImpl;
import mojo.dao.core.jpa.exec.SelectDetailImpl;
import mojo.dao.core.jpa.exec.UpdateDetailImpl;
import mojo.dao.core.util.Properties;

public class JpaSubRepository<E> extends JpaRepository<E> {

	private static final Logger logger = LoggerFactory.getLogger(JpaSubRepository.class);

	private Class<?> masterType;
	private String propertyName;
	private boolean inverseLink;

	public JpaSubRepository() {
		addExecutor(new SelectDetailImpl<E>());
		addExecutor(new InsertDetailImpl<E>());
		addExecutor(new UpdateDetailImpl<E>());
	}

	public Class<?> getMasterType() {
		return masterType;
	}

	public void setMasterType(Class<?> masterType) {
		this.masterType = masterType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Defines at which side the controlling link property resides.
	 * <ul>
	 * <li>False: property resides to detail,<br>
	 * i.e. Many-to-One, Many-to-One / One-to-Many</li>
	 * <li>True: property resides to master,<br>
	 * i.e. One-to-Many, One-to-Many / Many-to-One (inverse)</li>
	 * </ul>
	 */
	public boolean isInverseLink() {
		return inverseLink;
	}

	public void setInverseLink(boolean inverseLink) {
		this.inverseLink = inverseLink;
	}

	@SuppressWarnings("unchecked")
	public void link(Object parent, E child) {
		Method accessor = null;

		try {
			if (!isInverseLink() && child != null) {
				logger.debug("Binding parent to child");
				accessor = Properties.descriptor(child.getClass(), propertyName).getWriteMethod();
				accessor.invoke(child, parent);
			}
			else if (isInverseLink() && parent != null) {
				logger.debug("Attaching child to parent");
				accessor = Properties.descriptor(parent.getClass(), propertyName).getReadMethod();
				Collection<E> collection = (Collection<E>) accessor.invoke(parent);
				collection.add(child);
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public void unlink(Object parent, E child) {
		Method accessor = null;

		try {
			if (!isInverseLink() && child != null) {
				logger.debug("Unbinding parent from child");
				accessor = Properties.descriptor(child.getClass(), propertyName).getWriteMethod();
				accessor.invoke(child, (Object) null);
			}
			else if (isInverseLink() && parent != null) {
				logger.debug("Dettaching child from parent");
				accessor = Properties.descriptor(parent.getClass(), propertyName).getReadMethod();
				Collection<E> collection = (Collection<E>) accessor.invoke(parent);
				collection.remove(child);
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
