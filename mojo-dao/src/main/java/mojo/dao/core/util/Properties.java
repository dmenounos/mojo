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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for bean properties.
 */
public class Properties {

	private static final Logger logger = LoggerFactory.getLogger(Properties.class);
	private static final Map<Class<?>, String> idProperties = new HashMap<Class<?>, String>();
	private static final Map<Class<?>, Map<String, PropertyDescriptor>> descriptors = new HashMap<Class<?>, Map<String, PropertyDescriptor>>();

	public static Object id(Class<?> klass, Object obj) {
		try {
			String idProperty = idProperty(klass);
			Method idPropertyGetter = descriptor(klass, idProperty).getReadMethod();
			return idPropertyGetter.invoke(obj);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String idProperty(Class<?> klass) {
		if (!idProperties.containsKey(klass)) {
			synchronized (idProperties) {
				if (!idProperties.containsKey(klass)) {
					idProperties.put(klass, findIdProperty(klass));
				}
			}
		}

		return idProperties.get(klass);
	}

	public static PropertyDescriptor descriptor(Class<?> klass, String propertyName) {
		if (!descriptors.containsKey(klass)) {
			synchronized (descriptors) {
				if (!descriptors.containsKey(klass)) {
					descriptors.put(klass, findDescriptors(klass));
				}
			}
		}

		return descriptors.get(klass).get(propertyName);
	}

	private static String findIdProperty(Class<?> klass) {
		while (!klass.equals(Object.class)) {
			logger.info("Searching id property name in class: " + klass.getName());

			for (Field field : klass.getDeclaredFields()) {
				String fieldName = field.getName();

				if (field.isAnnotationPresent(Id.class)) {
					logger.info("Found id annotation on field: " + fieldName);
					return fieldName;
				}
			}

			for (Method method : klass.getDeclaredMethods()) {
				String methodName = method.getName();

				if (method.isAnnotationPresent(Id.class)) {
					logger.info("Found id annotation on method: " + methodName);
					return Introspector.decapitalize(methodName.substring(3));
				}
			}

			klass = klass.getSuperclass();
		}

		throw new RuntimeException("Reached the top of class hierarchy but didn't find id property name.");
	}

	private static Map<String, PropertyDescriptor> findDescriptors(Class<?> klass) {
		logger.info("Gathering property descriptors for class: " + klass.getName());

		try {
			Map<String, PropertyDescriptor> descriptors = new HashMap<String, PropertyDescriptor>();
			BeanInfo beanInfo = Introspector.getBeanInfo(klass);

			for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
				descriptors.put(descriptor.getName(), descriptor);
			}

			return descriptors;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
