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

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mojo.web.core.WebContextHolder;

public abstract class UIComponent {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private String id;
	private UIContainer parent;

	private int viewIndex;
	private List<String> viewStack;

	/**
	 * Setup the component hierarchy.
	 */
	public UIComponent() {
		setId(getClass().getSimpleName());
		viewStack = UIComponentViews.resolveViews(getClass());
	}

	/**
	 * This should be called by the tags or the controller.
	 */
	public void render() {
		if (viewStack.isEmpty()) {
			throw new RuntimeException(getClass().getName() + " has no view.");
		}

		if (viewIndex >= viewStack.size()) {
			throw new RuntimeException(getClass().getName() + " has already been rendered.");
		}

		onRender();

		UIContainer container = (UIContainer) getRequest().getAttribute("bean");
		UIComponent component = this;

		String compPath = component.getPath();
		String nextView = component.nextView();

		logger.debug("CONTAINER: {}", container);
		logger.debug("COMPONENT: {}", component);
		logger.debug("COMP-PATH: {}", compPath);
		logger.debug("NEXT-VIEW: {}", nextView);

		// setup render context
		getRequest().setAttribute("bean", component);
		getRequest().setAttribute(compPath, component);

		try {
			// do the actual jsp render
			RequestDispatcher rd = getRequest().getRequestDispatcher(nextView);
			rd.include(getRequest(), getResponse());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

		// restore render context
		getRequest().setAttribute("bean", container);
	}

	/**
	 * Override to execute custom code at render time.
	 */
	public void onRender() {
	}

	/**
	 * Gets the id of this component.
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id == null || id.isEmpty()) {
			throw new RuntimeException("Empty component id.");
		}

		this.id = id;
	}

	/**
	 * Gets any parent container, or null if there is none.
	 */
	public UIContainer getParent() {
		return parent;
	}

	protected void setParent(UIContainer parent) {
		this.parent = parent;
	}

	/**
	 * Gets this component's path.
	 */
	public final String getPath() {
		StringBuilder sb = new StringBuilder();
		String separator = ":";

		for (UIComponent comp = this; comp != null; comp = comp.getParent()) {
			sb.insert(0, comp.getId());

			if (comp.getParent() != null) {
				sb.insert(0, separator);
			}
		}

		return sb.toString();
	}

	protected String nextView() {
		return "/WEB-INF/jsp/" + viewStack.get(viewIndex++) + ".jsp";
	}

	protected boolean isAjaxRequest() {
		String requestedWith = getRequest().getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestedWith);
	}

	protected HttpServletRequest getRequest() {
		return WebContextHolder.getCurrentContext().getRequest();
	}

	protected HttpServletResponse getResponse() {
		return WebContextHolder.getCurrentContext().getResponse();
	}
}
