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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

import mojo.gwt.http.client.WebRequestBuilder;
import mojo.gwt.http.client.WebRequestCallback;
import mojo.gwt.ui.client.activity.BaseActivity;
import mojo.gwt.ui.client.activity.ClientFactory;
import mojo.gwt.ui.client.activity.page.PageActivity.PagePlace;
import mojo.gwt.ui.client.util.WebUtils;

/**
 * Strips specific regions out of full page contents, and wraps them in
 * HTMLPanel views.
 */
public class PageActivity extends BaseActivity<PagePlace> {

	public static final String POPUP_VALUE = "popup";

	public static final String OUTER_ALPHA = "<!-- outer-alpha -->";
	public static final String OUTER_OMEGA = "<!-- outer-omega -->";

	public static final String INNER_ALPHA = "<!-- inner-alpha -->";
	public static final String INNER_OMEGA = "<!-- inner-omega -->";

	public PageActivity(ClientFactory clientFactory, PagePlace place) {
		super(clientFactory, place);
	}

	@Override
	public void start(final AcceptsOneWidget container, EventBus eventBus) {
		final PagePlace place = getPlace();

		if (place.uri == null) {
			place.uri = Window.Location.getHref();
			return;
		}

		WebUtils.showLoadingMask();

		WebRequestBuilder builder = new WebRequestBuilder(RequestBuilder.GET, place.uri);
		builder.setCallback(new WebRequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				String contentType = response.getHeader("Content-Type");

				if (contentType == null || contentType.isEmpty()) {
					return;
				}

				switch (response.getStatusCode()) {
				case Response.SC_OK:
					if (contentType.startsWith("text/html")) {
						String text = response.getText();

						// comments mark the beginning and ending bounds
						// of the content region that will be stripped out
						String[] comments = { OUTER_ALPHA, OUTER_OMEGA };

						if (POPUP_VALUE.equals(place.getRel())) {
							// custom content regions can be specified
							// by setting a popup rel attribute on links
							comments[0] = INNER_ALPHA;
							comments[1] = INNER_OMEGA;
						}

						int bgn = text.indexOf(comments[0]);
						int end = text.indexOf(comments[1]);

						if (bgn != -1 && end != -1) {
							end += comments[1].length();

							// strip out the matched content
							String html = text.substring(bgn, end);

							HTMLPanel panel = new HTMLPanel(html);
							container.setWidget(panel);
						}
					}
					else if (contentType.startsWith("image/")) {
						Image image = new Image(place.uri);
						container.setWidget(image);
					}

					break;
				case Response.SC_FORBIDDEN:
					// oops, delegate to clientFactory
					getClientFactory().requireLogin();
					break;
				}

				WebUtils.hideLoadingMask();
			}

			/*
			private void debugInfo(Request request, Response response) {
				StringBuilder sb = new StringBuilder("WebRequestCallback.onResponseReceived ");
				sb.append(" statusCode: " + response.getStatusCode());
				GWT.log(sb.toString());

				for (Header header : response.getHeaders()) {
					sb = new StringBuilder("WebRequestCallback.onResponseReceived ");
					sb.append(header.getName() + ": " + header.getValue());
					GWT.log(sb.toString());
				}
			}
			*/
		});

		builder.send();
	}

	/**
	 * Carries activity parameters.
	 */
	public static class PagePlace extends Place {

		private String rel;
		private String uri;

		public PagePlace() {
			this(null, null);
		}

		public PagePlace(String rel, String uri) {
			this.rel = rel;
			this.uri = uri;
		}

		public boolean hasRel() {
			return rel != null && !rel.isEmpty();
		}

		public boolean hasUri() {
			return uri != null && !uri.isEmpty();
		}

		public String getRel() {
			return rel;
		}

		public void setRel(String rel) {
			this.rel = rel;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}
	}

	/**
	 * Converts place to / from uri compatible form.
	 */
	@Prefix("page")
	public static class PageTokenizer implements PlaceTokenizer<PagePlace> {

		/**
		 * Called *before* the activity is started; e.g. when navigating through
		 * the browser history buttons.
		 * 
		 * PlaceHistoryHandler.handleHistoryToken(String) -> <br>
		 * PlaceHistoryMapper.getPlace(String) -> <br>
		 * PlaceTokenizer.getPlace(String)
		 */
		@Override
		public PagePlace getPlace(String token) {
			String[] tokenParts = token.split(";");
			PagePlace place = new PagePlace();

			if (tokenParts.length >= 1) {
				place.setUri(tokenParts[tokenParts.length - 1]);
			}

			if (tokenParts.length >= 2) {
				place.setRel(tokenParts[tokenParts.length - 2]);
			}

			return place;
		}

		/**
		 * Called *after* the activity is started (but not the first time).
		 * 
		 * PlaceHistoryHandler.tokenForPlace(Place) -> <br>
		 * PlaceHistoryMapper.getToken(Place) -> <br>
		 * PlaceTokenizer.getToken(Place)
		 */
		@Override
		public String getToken(PagePlace place) {
			StringBuilder sb = new StringBuilder();

			if (place.hasRel()) {
				sb.append(place.getRel()).append(";");
			}

			if (place.hasUri()) {
				sb.append(place.getUri());
			}

			return sb.toString();
		}
	}
}
