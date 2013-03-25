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
package mojo.gwt.ui.client.activity.portal;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import mojo.gwt.ui.client.activity.page.PageActivity;
import mojo.gwt.ui.client.activity.page.PageActivity.PagePlace;
import mojo.gwt.ui.client.activity.page.PageContainer;
import mojo.gwt.ui.client.util.WebUtils;

public class PortalContainer extends PageContainer {

	public static PortalContainer wrap(Element element) {
		// assert that the element is attached
		assert Document.get().getBody().isOrHasChild(element);

		PortalContainer panel = new PortalContainer(element);

		// initialize life cycle
		panel.onAttach();

		// remember it for cleanup
		RootPanel.detachOnWindowClose(panel);

		return panel;
	}

	protected PortalContainer(Element element) {
		super(element);
	}

	@Override
	public void setWidget(IsWidget isWidget) {
		if (isWidget == null) return;

		WebUtils.closePopup();

		String scripts = extractScripts(isWidget.asWidget());
		Place place = getClientFactory().getPlaceController().getWhere();

		if (place instanceof PagePlace) {
			PagePlace pagePlace = (PagePlace) place;

			if (PageActivity.POPUP_VALUE.equals(pagePlace.getRel())) {
				Widget widget = isWidget.asWidget();
				WebUtils.openPopup(widget);
				executeScripts(scripts);
				return;
			}
		}

		// default behavior
		super.setWidget(isWidget);
		executeScripts(scripts);
	}

	protected String extractScripts(Widget widget) {
		if (widget instanceof HTMLPanel) {
			HTMLPanel panel = (HTMLPanel) widget;
			String html = panel.getElement().getInnerHTML();
			return doExtractScripts(html);
		}

		return null;
	}

	protected void executeScripts(String scripts) {
		if (scripts != null && !scripts.isEmpty()) {
			doExecuteScripts(scripts);
		}
	}

	protected native String doExtractScripts(String text)
	/*-{
		var scripts = '';
		text.replace(/<script[^>]*>([\s\S]*?)<\/script>/gi, function() {
			scripts += arguments[1] + '\n';
			return '';
		});
		return scripts;
	}-*/;

	protected native void doExecuteScripts(String scripts)
	/*-{
		if ($wnd.execScript){
			$wnd.execScript(scripts);
		} else {
			var head = $wnd.document.getElementsByTagName('head')[0];
			var scriptElement = $wnd.document.createElement('script');
			scriptElement.setAttribute('type', 'text/javascript');
			scriptElement.appendChild(document.createTextNode(scripts));
			head.appendChild(scriptElement);
		}
	}-*/;
}
