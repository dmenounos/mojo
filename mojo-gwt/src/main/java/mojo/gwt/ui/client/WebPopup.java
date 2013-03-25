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
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasLoadHandlers;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * A closable popup panel.
 */
public class WebPopup extends PopupPanel {

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

	public WebPopup() {
		addStyleName(css.WebPopup());
		setAutoHideEnabled(true);
		setGlassEnabled(false);
		setClosable(false);
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
			getElement().appendChild(getCloseImage().getElement());
			adopt(getCloseImage());
		}
	}

	@Override
	protected void onUnload() {
		if (isClosable()) {
			orphan(getCloseImage());
			getElement().removeChild(getCloseImage().getElement());
		}

		super.onUnload();
	}

	/**
	 * The show / hide implementation through animation is broken beyond repair.
	 * Private resizeAnimation, ANIMATION_DURATION. No start / complete
	 * callback methods. Convoluted state handling.
	 * 
	 * Its a f-u-c-k-i-n-g mess.
	 */
	@Override
	public void show() {
		boolean isAnimated = isAnimationEnabled();
		boolean isLoadWidget = getWidget() instanceof HasLoadHandlers;
		setAnimationEnabled(isAnimated && !isLoadWidget);
		super.show();
	}

	@Override
	public void center() {
		if (getWidget() instanceof HasLoadHandlers) {
			HasLoadHandlers hasLoadHandlers = (HasLoadHandlers) getWidget();
			hasLoadHandlers.addLoadHandler(new LoadHandler() {

				@Override
				public void onLoad(LoadEvent event) {
					doCenter();
					setVisible(true);
				}
			});
		}

		// hide until the widget has been loaded
		setVisible(false);

		// attach our element into DOM
		show();

		if (!(getWidget() instanceof HasLoadHandlers)) {
			doCenter();
			setVisible(true);
		}
	}

	protected void doCenter() {
		// if we have not been shown
		// the super.center() will call show()
		// in order to attach our element into DOM

		super.center();

		// if we have not been shown and have animation
		// the super.center() will have the animation started

		Timer timer = new Timer() {

			@Override
			public void run() {
				// browser workarounds
				getElement().getStyle().setProperty("clip", "auto");
				getCloseImage().getElement().getStyle().setProperty("display", "block");
			}
		};

		// animation duration is 200 milliseconds
		// frame time is either 25 or 16 milliseconds
		timer.schedule(isAnimationEnabled() ? 250 : 0);
	}

	public interface Resources extends ClientBundle {

		@Source("WebPopupClose.png")
		ImageResource closeImage();

		@Source("WebPopup.css")
		Style style();
	}

	public interface Style extends CssResource {

		String WebPopup();

		String withGlass();

		String closeImage();
	}
}
