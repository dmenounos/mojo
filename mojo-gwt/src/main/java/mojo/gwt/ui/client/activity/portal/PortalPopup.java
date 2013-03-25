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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

import mojo.gwt.ui.client.WebPopup;

public class PortalPopup extends WebPopup {

	private static final Resources res;
	private static final Style css;

	static {
		res = GWT.create(Resources.class);
		res.style().ensureInjected();
		StyleInjector.flush();
		css = res.style();
	}

	public PortalPopup() {
		addStyleName(css.PortalPopup());
		setAnimationEnabled(true);
		setGlassEnabled(true);
		setClosable(true);
	}

	@Override
	public void show() {
		super.show();

		float dw = Document.get().getClientWidth();
		float dh = Document.get().getClientHeight();
		float dr = dw / dh;

		float ew = getContainerElement().getClientWidth();
		float eh = ew / dr;

		String wstr = Math.round(ew) + 20 + "px";
		String hstr = Math.round(eh) + 20 + "px";

		getContainerElement().getStyle().setProperty("width", wstr);
		getContainerElement().getStyle().setProperty("height", hstr);
	}

	public interface Resources extends ClientBundle {

		@Source("PortalPopup.css")
		Style style();
	}

	public interface Style extends CssResource {

		String PortalPopup();
	}
}
