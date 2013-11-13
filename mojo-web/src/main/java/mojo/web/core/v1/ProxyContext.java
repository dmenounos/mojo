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
package mojo.web.core.v1;

import javax.servlet.http.HttpServletRequest;

import mojo.web.core.BaseContext;

/**
 * Provides the servlet request from our thread local storage.<br />
 * Works in conjunction with either WebContextFilter or WebContextInterceptor.
 */
public class ProxyContext extends BaseContext {

	protected HttpServletRequest getRequest() {
		return WebContextHolder.getCurrentContext().getRequest();
	}
}
