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
package mojo.dao.core.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import org.hibernate.Hibernate;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentSet;
import org.hibernate.collection.PersistentSortedSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XStream Hibernate collection converter.
 */
public class XHCollectionConverter implements Converter {

	private static final Logger logger = LoggerFactory.getLogger(XHCollectionConverter.class);

	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return PersistentCollection.class.isAssignableFrom(type);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		logger.debug("### Hibernate collection");

		if (Hibernate.isInitialized(source)) {
			logger.debug("### Unwrapping value");
			PersistentCollection proxy = (PersistentCollection) source;
			Object collection = proxy.getStoredSnapshot();

			// Hibernate Set snapshots are HashMaps
			logger.debug("### Unwrapped value class: " + collection.getClass());

			if (proxy instanceof PersistentSortedSet) {
				collection = new TreeSet(((HashMap) collection).values());
			}
			else if (proxy instanceof PersistentSet) {
				collection = new HashSet(((HashMap) collection).values());
			}

			context.convertAnother(collection);
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		return null;
	}
}
