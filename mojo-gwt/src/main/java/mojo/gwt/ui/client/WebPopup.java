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
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
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
		if (getWidget() instanceof HasLoadHandlers) {
			HasLoadHandlers hasLoadHandlers = (HasLoadHandlers) getWidget();
			hasLoadHandlers.addLoadHandler(new LoadHandler() {

				@Override
				public void onLoad(LoadEvent event) {
					callback.setPosition(getOffsetWidth(), getOffsetHeight());
					setVisible(true);
				}
			});
		}

		setVisible(false);
		show();

		if (!(getWidget() instanceof HasLoadHandlers)) {
			callback.setPosition(getOffsetWidth(), getOffsetHeight());
			setVisible(true);
		}
	}

	@Override
	public void center() {
		setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				prepareCenterDimension();
				prepareCenterPosition();
				resizeAnimation.setState(true);
			}
		});
	}

	protected void prepareCenterDimension() {
		// Reset element position and dimensions.
		// If left/top are set from a previous call, and our content has
		// changed, we may get a bogus getOffsetWidth because our new
		// content is wrapping (giving a lower offset width) then it would
		// without the previous left. Setting left/top back to 0 avoids this.
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
		// When the widget is larger than the view-port, the calculated
		// space from previous step will be negative, so resort to 0.
		left = Math.max(Window.getScrollLeft() + left, 0);
		top  = Math.max(Window.getScrollTop()  + top,  0);

		setPopupPosition(left, top);
		getElement().getStyle().setProperty("position", "absolute");
	}

	/**
	 * Show or hide the glass.
	 */
	protected void maybeShowGlass(boolean showing) {
		if (isGlassEnabled()) {
			if (showing) {
				Document.get().getBody().appendChild(getGlassElement());

				// resizeRegistration = Window.addResizeHandler(curPanel.glassResizer);
				// curPanel.glassResizer.onResize(null);
			}
			else {
				Document.get().getBody().removeChild(getGlassElement());

				// resizeRegistration.removeHandler();
				// resizeRegistration = null;
			}
		}
	}

	protected static final int ANIMATION_DURATION = 200;
	protected AnimationType animType = AnimationType.CENTER;
	protected ResizeAnimation resizeAnimation = new ResizeAnimation(this);

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

		// private HandlerRegistration resizeRegistration;

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
			popup.maybeShowGlass(showing);
			offsetWidth = popup.getOffsetWidth();
			offsetHeight = popup.getOffsetHeight();
			setClip(popup.getElement(), getRectString(0, 0, 0, 0));
			popup.getElement().getStyle().setProperty("overflow", "hidden");
			super.onStart();
		}

		@Override
		protected void onComplete() {
			setClip(popup.getElement(), "auto");
			popup.getElement().getStyle().setProperty("overflow", "visible");
			popup.getCloseImage().getElement().getStyle().setProperty("display", "block");
			popup.maybeShowGlass(showing);
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
			setClip(popup.getElement(), getRectString(top, right, bottom, left));
		}

		private void setClip(Element popup, String rect) {
			popup.getStyle().setProperty("clip", rect);
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
