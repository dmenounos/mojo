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
package mojo.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mojo.dao.AuditContext;

public class RequireLoginInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(RequireLoginInterceptor.class);

	@Autowired
	@Qualifier("auditContext")
	private AuditContext context;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object con) throws Exception {
		logger.debug("Handler: {}", (con != null ? con.getClass().getName() : null));

		if (context.getUser() == null && con.getClass().isAnnotationPresent(RequireLogin.class)) {
			logger.debug("Forbidden Controller: " + con.getClass().getName());

			if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}

			throw new RequireLoginException();
		}

		return true;
	}
}
