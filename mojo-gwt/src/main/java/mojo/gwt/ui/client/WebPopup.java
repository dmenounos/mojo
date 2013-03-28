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

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasLoadHandlers;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
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

	private boolean animationEnabled;

	public boolean hasAnimationEnabled() {
		return animationEnabled;
	}

	@Override
	public void setAnimationEnabled(boolean enabled) {
		this.animationEnabled = enabled;
	}

	@Override
	public void setPopupPositionAndShow(final PositionCallback callback) {
		boolean loadWidget = getWidget() instanceof HasLoadHandlers;

		if (loadWidget) {
			HasLoadHandlers hasLoadHandlers = (HasLoadHandlers) getWidget();
			hasLoadHandlers.addLoadHandler(new LoadHandler() {

				@Override
				public void onLoad(LoadEvent event) {
					callback.setPosition(getOffsetWidth(), getOffsetHeight());
					setVisible(true);
					resizeAnimation.setState(true);
				}
			});
		}

		setVisible(false);
		show();

		if (!loadWidget) {
			callback.setPosition(getOffsetWidth(), getOffsetHeight());
			setVisible(true);
			resizeAnimation.setState(true);
		}
	}

	@Override
	public void center() {
		setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				prepareCenterDimension();
				prepareCenterPosition();
			}
		});
	}

	protected void prepareCenterDimension() {
		// Reset element position and dimensions.
		getElement().getStyle().setPropertyPx("left", 0);
		getElement().getStyle().setPropertyPx("top",  0);
		getElement().getStyle().clearWidth();
		getElement().getStyle().clearHeight();
	}

	protected void prepareCenterPosition() {
		// Calculate space in between view-port and widget.
		int left = (Window.getClientWidth()  - getOffsetWidth())  >> 1;
		int top  = (Window.getClientHeight() - getOffsetHeight()) >> 1;

		// Add possible scroll offset.
		left = Window.getScrollLeft() + left;
		top  = Window.getScrollTop()  + top;

		setPopupPosition(left, top);
		getElement().getStyle().setProperty("position", "absolute");
	}

	protected static final int ANIMATION_DURATION = 200;
	protected AnimationType animType = AnimationType.CENTER;
	protected ResizeAnimation resizeAnimation = createResizeAnimation();

	protected ResizeAnimation createResizeAnimation() {
		return new ResizeAnimation(this);
	}

	protected static enum AnimationType {
		CENTER, ONE_WAY_CORNER, ROLL_DOWN
	}

	protected static class ResizeAnimation extends Animation {

		/**
		 * The {@link PopupPanel} being affected.
		 */
		private WebPopup popup;

		/**
		 * A boolean indicating whether we are showing or hiding the popup.
		 */
		private boolean showing;

		/**
		 * The offset height and width of the current {@link PopupPanel}.
		 */
		private int offsetHeight, offsetWidth = -1;

		public ResizeAnimation(WebPopup panel) {
			this.popup = panel;
		}

		public void setState(boolean showing) {
			cancel();
			this.showing = showing;
			run(popup.hasAnimationEnabled() ? ANIMATION_DURATION : 0);
		}

		@Override
		protected void onStart() {
			offsetWidth = popup.getOffsetWidth();
			offsetHeight = popup.getOffsetHeight();

			popup.getElement().getStyle().setProperty("clip", getRectString(0, 0, 0, 0));

			if (popup.isClosable()) {
				popup.getCloseImage().getElement().getStyle().setProperty("display", "none");
			}

			super.onStart();
		}

		@Override
		protected void onComplete() {
			super.onComplete();

			popup.getElement().getStyle().clearProperty("clip");

			if (popup.isClosable()) {
				popup.getCloseImage().getElement().getStyle().setProperty("display", "block");
			}
		}

		@Override
		protected void onUpdate(double progress) {
			if (!showing) {
				progress = 1.0 - progress;
			}

			// Determine the clipping size
			int top = 0;
			int left = 0;
			int right = 0;
			int bottom = 0;
			int width = (int) (progress * offsetWidth);
			int height = (int) (progress * offsetHeight);

			switch (popup.animType) {
			case ROLL_DOWN:
				right = offsetWidth;
				bottom = height;
				break;
			case CENTER:
				top = (offsetHeight - height) >> 1;
				left = (offsetWidth - width) >> 1;
				right = left + width;
				bottom = top + height;
				break;
			case ONE_WAY_CORNER:
				if (LocaleInfo.getCurrentLocale().isRTL()) {
					left = offsetWidth - width;
				}
				right = left + width;
				bottom = top + height;
				break;
			}

			// Set the rect clipping
			String rect = getRectString(top, right, bottom, left);
			popup.getElement().getStyle().setProperty("clip", rect);
		}

		private String getRectString(int top, int right, int bottom, int left) {
			return "rect(" + top + "px, " + right + "px, " + bottom + "px, " + left + "px)";
		}
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
