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
package mojo.view.util;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DebugUtils {

	private static final Logger logger = LoggerFactory.getLogger(DebugUtils.class);

	public static void logRequestInfo(HttpServletRequest req) {
		logger.debug("session.id          : " + req.getSession().getId());
		logger.debug("request.method      : " + req.getMethod());
		logger.debug("request.pathInfo    : " + req.getPathInfo());
		logger.debug("request.requestURI  : " + req.getRequestURI());
		logger.debug("request.requestURL  : " + req.getRequestURL());
		logger.debug("request.queryString : " + req.getQueryString());
		logger.debug("");

		logRequestHeaders(req);
		logRequestParameters(req);
		logRequestAttributes(req);
	}

	@SuppressWarnings("rawtypes")
	public static void logRequestHeaders(HttpServletRequest req) {
		logger.debug("REQUEST HEADERS");
		logger.debug("---------------");

		Enumeration enums = req.getHeaderNames();

		while (enums.hasMoreElements()) {
			String headerName = (String) enums.nextElement();
			String headerValue = req.getHeader(headerName);
			logger.debug(headerName + ": " + headerValue);
		}

		logger.debug("");
	}

	@SuppressWarnings("rawtypes")
	public static void logRequestParameters(HttpServletRequest req) {
		logger.debug("REQUEST PARAMETERS");
		logger.debug("------------------");

		Enumeration enums = req.getParameterNames();

		while (enums.hasMoreElements()) {
			String paramName = (String) enums.nextElement();
			String[] paramValues = req.getParameterValues(paramName);
			StringBuilder sb = new StringBuilder();
			sb.append(paramName).append(": " + paramValues);
			logger.debug(sb.toString());
		}

		logger.debug("");
	}

	@SuppressWarnings("rawtypes")
	public static void logRequestAttributes(HttpServletRequest req) {
		logger.debug("REQUEST ATTRIBUTES");
		logger.debug("------------------");

		Enumeration enums = req.getAttributeNames();

		while (enums.hasMoreElements()) {
			String attrName = (String) enums.nextElement();
			Object attrValue = req.getAttribute(attrName);
			StringBuilder sb = new StringBuilder();
			sb.append(attrName + ": " + attrValue);
			logger.debug(sb.toString());
		}

		logger.debug("");
	}

	public static void logBeanNames(ApplicationContext ctx) {
		System.out.println(ctx.getClass().getName() + " bean names:");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);

		for (String beanName : beanNames) {
			System.out.println("\t" + beanName);
		}
	}
}
