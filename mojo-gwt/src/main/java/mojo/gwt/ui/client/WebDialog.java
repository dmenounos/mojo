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
package mojo.gwt.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;

/**
 * A closable dialog box.
 */
public class WebDialog extends DialogBox {

	private static final Resources res;
	private static final Style css;

	static {
		res = GWT.create(Resources.class);
		res.style().ensureInjected();
		StyleInjector.flush();
		css = res.style();
	}

	private boolean closable;
	private Image closeImage;

	public WebDialog() {
		addStyleName(css.WebDialog());
		setAutoHideEnabled(false);
		setGlassEnabled(true);
		setClosable(true);
	}

	public boolean isClosable() {
		return closable;
	}

	public void setClosable(boolean closable) {
		this.closable = closable;
	}

	public Image getCloseImage() {
		if (closeImage == null) {
			closeImage = new Image(res.closeImage());
			closeImage.addStyleName(css.closeImage());
			closeImage.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					hide();
				}
			});
		}

		return closeImage;
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		if (isGlassEnabled()) {
			addStyleName(css.withGlass());
		}

		if (isClosable()) {
			Element topCenterInner = getCellElement(0, 1);
			topCenterInner.insertFirst(getCloseImage().getElement());
			adopt(getCloseImage());
		}
	}

	@Override
	protected void onUnload() {
		if (isClosable()) {
			orphan(getCloseImage());
			Element topCenterInner = getCellElement(0, 1);
			topCenterInner.removeChild(getCloseImage().getElement());
		}

		super.onUnload();
	}

	public interface Resources extends ClientBundle {

		@Source("WebDialogClose.png")
		ImageResource closeImage();

		@Source("WebDialog.css")
		Style style();
	}

	public interface Style extends CssResource {

		String WebDialog();

		String withGlass();

		String closeImage();
	}
}
