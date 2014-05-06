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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class UIChildTag extends BaseTag {

	@Override
	public void doTag() throws JspException, IOException {
		logger.debug("-->");

		PageContext pageContext = (PageContext) getJspContext();

		UIComponent component = getAttribute("bean", PageContext.REQUEST_SCOPE);

		if (component == null) {
			throw new RuntimeException("Component 'bean' is null");
		}

		pageContext.getOut().flush();

		component.render();

		pageContext.getOut().flush();

		logger.debug("<--");
	}
}
