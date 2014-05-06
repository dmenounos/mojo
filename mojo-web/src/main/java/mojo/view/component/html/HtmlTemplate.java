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
package mojo.web.component.html;

import java.util.ArrayList;
import java.util.List;

import mojo.web.component.UIContainer;

public class HtmlTemplate extends UIContainer {

	private String title;
	private List<String> styles;
	private List<String> scripts;

	protected HtmlTemplate() {
		String contextPath = getRequest().getContextPath();

		// Init global jsp attributes.
		getRequest().setAttribute("contextPath", contextPath);
		getRequest().setAttribute("xmlHttpRequest", isAjaxRequest());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getStyles() {
		if (styles == null) {
			styles = new ArrayList<String>();
		}

		return styles;
	}

	public List<String> getScripts() {
		if (scripts == null) {
			scripts = new ArrayList<String>();
		}

		return scripts;
	}

	@Override
	protected String nextView() {
		String result = super.nextView();

		if (isAjaxRequest() && result.contains("HtmlTemplate")) {
			result = result.replace("HtmlTemplate", "HtmlTemplateAjax");
		}

		return result;
	}
}
