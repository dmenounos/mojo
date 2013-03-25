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
import com.google.gwt.user.client.ui.Image;

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
	protected void doCenter() {
		float documentWidth  = Document.get().getClientWidth();
		float documentHeight = Document.get().getClientHeight();
		float documentRatio  = documentWidth / documentHeight;

		if (getWidget() instanceof Image) {
			int decorationSize = 80; // approximately

			// calculate our outer dimensions, including decoration
			float outerWidth  = getElement().getClientWidth()  + decorationSize;
			float outerHeight = getElement().getClientHeight() + decorationSize;

			if (outerWidth > documentWidth || outerHeight > documentHeight) {

				// our outer dimensions are larger than the browser view-port.

				float imageWidth  = getWidget().getElement().getClientWidth();
				float imageHeight = getWidget().getElement().getClientHeight();
				float imageRatio  = imageWidth / imageHeight;

				// scale-in the inner image by calculating
				// a ratio based on its larger dimension

				float scaleRatio = 1.0f;

				if (imageRatio > documentRatio) {
					float margin = outerWidth - imageWidth;
					scaleRatio = (documentWidth - margin) / imageWidth;
				}
				else if (imageRatio < documentRatio) {
					float margin = outerHeight - imageHeight;
					scaleRatio = (documentHeight - margin) / imageHeight;
				}

				imageWidth  *= scaleRatio;
				imageHeight *= scaleRatio;

				String widthString  = Math.round(imageWidth)  + "px";
				String heightString = Math.round(imageHeight) + "px";

				getWidget().getElement().getStyle().setProperty("width",  widthString);
				getWidget().getElement().getStyle().setProperty("height", heightString);
			}
		}
		else {
			float innerWidth  = getContainerElement().getClientWidth();
			float innerHeight = innerWidth / documentRatio;

			int scrollbarsSize = 20; // approximately

			String widthString  = Math.round(innerWidth)  + scrollbarsSize + "px";
			String heightString = Math.round(innerHeight) + scrollbarsSize + "px";

			getContainerElement().getStyle().setProperty("width",  widthString);
			getContainerElement().getStyle().setProperty("height", heightString);
		}

		super.doCenter();
	}

	public interface Resources extends ClientBundle {

		@Source("PortalPopup.css")
		Style style();
	}

	public interface Style extends CssResource {

		String PortalPopup();
	}
}
