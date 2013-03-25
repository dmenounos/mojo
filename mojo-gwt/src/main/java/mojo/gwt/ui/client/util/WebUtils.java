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
package mojo.gwt.ui.client.util;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import mojo.gwt.ui.client.WebDialog;
import mojo.gwt.ui.client.activity.portal.PortalPopup;

public class WebUtils {

	// http://www.regular-expressions.info/email.html
	public static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$";

	/** Global modal popup. */
	private static PortalPopup popup;

	public static void closePopup() {
		if (popup != null) {
			popup.hide();
		}
	}

	public static void openPopup(Widget widget) {
		if (popup == null) {
			popup = new PortalPopup();
			popup.addCloseHandler(new CloseHandler<PopupPanel>() {

				@Override
				public void onClose(CloseEvent<PopupPanel> event) {
					popup = null;
				}
			});
			popup.setWidget(widget);
			popup.center();
		}
	}

	public static void alert(String title, String message) {
		WebDialog dialog = new WebDialog();
		dialog.addStyleName("alert-box");
		dialog.setAnimationEnabled(true);
		dialog.setModal(true);
		dialog.setText(title);

		HTML messageLabel = new HTML(message);
		messageLabel.addStyleName("content");

		dialog.setWidget(messageLabel);
		dialog.center();
	}

	public static void tip(String message, UIObject target) {
		HTML messageLabel = new HTML(message);
		messageLabel.addStyleName("content");

		PopupPanel tip = new DecoratedPopupPanel(true);
		tip.addStyleName("tip-box");
		tip.setWidget(messageLabel);
		tip.showRelativeTo(target);
	}

	/**
	 * Show loading mask.
	 */
	public static native void showLoadingMask()
	/*-{
		if ($wnd.showLoadingMask) {
			$wnd.showLoadingMask();
		}
	}-*/;

	/**
	 * Hide loading mask.
	 */
	public static native void hideLoadingMask()
	/*-{
		if ($wnd.hideLoadingMask) {
			$wnd.hideLoadingMask();
		}
	}-*/;
}
