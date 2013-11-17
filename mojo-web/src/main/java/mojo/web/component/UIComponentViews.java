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
package mojo.web.component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIComponentViews {

	private static final Logger logger = LoggerFactory.getLogger(UIComponentViews.class);

	private static final Map<String, List<String>> views = new HashMap<String, List<String>>();

	private static boolean checkResource(String path) {
		return UIComponentViews.class.getResource(path) != null;
	}

	public static List<String> resolveViews(Class<?> klass) {
		String klassName = klass.getName();

		if (views.containsKey(klassName)) {
			return views.get(klassName);
		}

		List<String> klassViews = new LinkedList<String>();

		while (UIComponent.class.isAssignableFrom(klass)) {
			String viewName = klass.getName().replace(".", "/");
			String viewPath = "/" + viewName + ".jsp";
			logger.debug("Looking path: " + viewPath);

			if (checkResource(viewPath)) {
				klassViews.add(0, viewName);
			}

			klass = klass.getSuperclass();
		}

		if (!klassViews.isEmpty()) {
			logger.info("Caching view: " + klassName + " " + klassViews);
			views.put(klassName, klassViews);
			return klassViews;
		}

		throw new RuntimeException("Could not resolve view for: " + klassName);
	}
}
