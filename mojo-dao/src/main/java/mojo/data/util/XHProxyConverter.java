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

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XStream Hibernate proxy converter.
 */
public class XHProxyConverter implements Converter {

	private static final Logger logger = LoggerFactory.getLogger(XHProxyConverter.class);

	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return HibernateProxy.class.isAssignableFrom(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		logger.debug("### Hibernate proxy");

		if (Hibernate.isInitialized(source)) {
			logger.debug("### Unwrapping value");
			HibernateProxy proxy = (HibernateProxy) source;
			LazyInitializer initializer = proxy.getHibernateLazyInitializer();
			context.convertAnother(initializer.getImplementation());
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0, UnmarshallingContext arg1) {
		return null;
	}
}
