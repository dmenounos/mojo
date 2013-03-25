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
package mojo.gwt.ui.client.activity.page;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import mojo.gwt.ui.client.activity.ClientFactory;
import mojo.gwt.ui.client.activity.page.PageActivity.PagePlace;

/**
 * Root page container.<br>
 * <br>
 * 1. Augments navigation by intercepting click events. Delegates responsibility
 * to PageActivity, through invoking PlaceController.goTo(PagePlace).<br>
 * <br>
 * 2. Wraps the main page element and hosts content by deriving from SimplePanel
 * and AcceptsOneWidget. By default all content is swapped on each update,
 * however subclasses may choose to implement more sophisticated behavior.
 */
public class PageContainer extends SimplePanel implements AcceptsOneWidget {

	private static final RegExp uriPattern = RegExp.compile("^https?://[^/]+/");

	private ClientFactory clientFactory;

	public static PageContainer wrap(Element element) {
		// assert that the element is attached
		assert Document.get().getBody().isOrHasChild(element);

		PageContainer panel = new PageContainer(element);

		// initialize life cycle
		panel.onAttach();

		// remember it for cleanup
		RootPanel.detachOnWindowClose(panel);

		return panel;
	}

	protected PageContainer(Element element) {
		super(element);
		sinkEvents(Event.ONCLICK);
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setClientFactory(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);

		switch (DOM.eventGetType(event)) {
		case Event.ONCLICK:
			onClick(event);
			break;
		}
	}

	/**
	 * Intercepts click events.
	 */
	protected void onClick(Event event) {
		Element eventEl = Element.as(event.getEventTarget());
		AnchorElement link = getClosestLink(eventEl);

		// event is not from a link
		// resort to default browser behavior
		if (link == null) {
			return;
		}

		//
		// check for sensitive attributes
		//

		String target = link.getTarget();
		String href = link.getHref();

		boolean hasTarget = !target.isEmpty();
		boolean hasFragment = href.indexOf("#") != -1;

		// resort to default browser behavior
		if (hasTarget || hasFragment) {
			return;
		}

		//
		// check for external link address
		//

		String currentBaseUri = getCurrentBaseUri();

		// resort to default browser behavior
		if (!href.startsWith(currentBaseUri)) {
			return;
		}

		// shorten link address
		href = href.substring(currentBaseUri.length() - 1);

		event.preventDefault();

		String rel = link.getRel();
		PagePlace contentPlace = new PagePlace(rel, href);
		getClientFactory().getPlaceController().goTo(contentPlace);
	}

	/**
	 * Finds the closest link by walking up the hierarchy, beginning from el.
	 */
	protected AnchorElement getClosestLink(Element el) {
		while (el != null && !el.equals(getElement())) {
			if (el.getNodeName().toLowerCase().equals("a")) {
				return AnchorElement.as(el);
			}

			el = el.getParentElement();
		}

		return null;
	}

	/**
	 * Returns the current realm. e.g.<br>
	 * http://localhost:8080/
	 */
	protected String getCurrentBaseUri() {
		String currentUri = Window.Location.getHref();
		MatchResult mr = uriPattern.exec(currentUri);
		currentUri = mr.getGroup(0);

		if (currentUri == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(Window.Location.getProtocol());
			sb.append("//");
			sb.append(Window.Location.getHost());

			if (!"80".equals(Window.Location.getPort())) {
				sb.append(":");
				sb.append(Window.Location.getPort());
			}

			sb.append("/");
			currentUri = sb.toString();
		}

		return currentUri;
	}

	/**
	 * Updates page content.
	 */
	@Override
	public void setWidget(IsWidget isWidget) {
		if (isWidget == null) return;

		// clean previous widget
		clear();

		// clean previous non-widget elements
		Element element = getElement();

		while (element.hasChildNodes()) {
			Node node = element.getChild(0);
			element.removeChild(node);
		}

		// add next widget
		setWidget(isWidget.asWidget());
	}
}
