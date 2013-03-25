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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentMap;
import org.hibernate.collection.PersistentSet;
import org.hibernate.collection.PersistentSortedMap;
import org.hibernate.collection.PersistentSortedSet;
import org.hibernate.proxy.HibernateProxy;

public class XHProxyMapper extends MapperWrapper {

	private Map<Class<?>, Class<?>> collections;

	public XHProxyMapper(Mapper wrapped) {
		super(wrapped);

		collections = new HashMap<Class<?>, Class<?>>();
		collections.put(PersistentBag.class, ArrayList.class);
		collections.put(PersistentList.class, ArrayList.class);
		collections.put(PersistentMap.class, HashMap.class);
		collections.put(PersistentSet.class, HashSet.class);
		collections.put(PersistentSortedMap.class, TreeMap.class);
		collections.put(PersistentSortedSet.class, TreeSet.class);
	}

	@SuppressWarnings("rawtypes")
	public String serializedClass(Class klass) {
		if (HibernateProxy.class.isAssignableFrom(klass)) {
			return klass.getSuperclass().getName();
		}

		if (collections.containsKey(klass)) {
			return collections.get(klass).getName();
		}

		return super.serializedClass(klass);
	}
}
