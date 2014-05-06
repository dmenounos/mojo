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
package mojo.view.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTag extends SimpleTagSupport {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected HttpServletRequest getRequest() {
		PageContext pageContext = (PageContext) getJspContext();
		return (HttpServletRequest) pageContext.getRequest();
	}

	protected HttpServletResponse getResponse() {
		PageContext pageContext = (PageContext) getJspContext();
		return (HttpServletResponse) pageContext.getRequest();
	}

	@SuppressWarnings("unchecked")
	protected <T> T getAttribute(String name, int scope) {
		PageContext pageContext = (PageContext) getJspContext();
		return (T) pageContext.getAttribute(name, scope);
	}

	protected <T> void setAttribute(String name, T value, int scope) {
		PageContext pageContext = (PageContext) getJspContext();
		pageContext.setAttribute(name, value, scope);
	}
}
