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
package mojo.view.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mojo.core.AuditContext;

public class WebContext implements AuditContext {

	/** User session attribute. */
	public static final String CONTEXT_USER_ATTR = "contextUser";

	@Override
	public Object getUser() {
		HttpSession session = getRequest().getSession();
		return session.getAttribute(CONTEXT_USER_ATTR);
	}

	@Override
	public String getRemoteUser() {
		return getRequest().getRemoteUser();
	}

	@Override
	public String getRemoteHost() {
		return getRequest().getRemoteHost();
	}

	@Override
	public boolean isUserInRole(String role) {
		return getRequest().isUserInRole(role);
	}

	public HttpServletRequest getRequest() {
		return WebContextHolder.getCurrentContext().getRequest();
	}

	public HttpServletResponse getResponse() {
		return WebContextHolder.getCurrentContext().getResponse();
	}
}
