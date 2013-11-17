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
package mojo.web.util;

import java.beans.Introspector;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static boolean containsBean(String id) {
		if (id == null || id.isEmpty()) {
			return false;
		}

		return applicationContext.containsBean(id);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String id, Object... args) {
		if (args != null && args.length == 0) {
			args = null;
		}

		return (T) applicationContext.getBean(id, args);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getComponent(Class<?> klass) {
		String name = Introspector.decapitalize(klass.getSimpleName());
		return (T) applicationContext.getBean(name, name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtils.applicationContext = applicationContext;
	}
}
