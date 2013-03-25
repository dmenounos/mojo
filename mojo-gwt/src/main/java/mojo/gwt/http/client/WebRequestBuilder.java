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
package mojo.gwt.http.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;

/**
 * Builder for constructing Request objects.
 */
public class WebRequestBuilder extends RequestBuilder {

	/**
	 * Creates a builder using the parameters for configuration.
	 * 
	 * @param httpMethod HTTP method to use for the request
	 * @param url URL that has already has already been encoded. Please see
	 *        URL.encode(String), URL.encodePathSegment(String) and
	 *        URL.encodeQueryString(String) for how to do this.
	 */
	public WebRequestBuilder(Method httpMethod, String url) {
		super(httpMethod, url);
	}

	/**
	 * Sets the data to send as part of this request. This method <b>must</b> be
	 * called before calling {@link send()}.
	 */
	public void setFormData(String formData) {
		setHeader("Content-Type", "application/x-www-form-urlencoded");
		setRequestData(formData);
	}

	/**
	 * Abstracts away tedious error handling by using a centralized
	 * {@link ErrorHandler error handler}.
	 */
	@Override
	public Request send() {
		try {
			return super.send();
		}
		catch (RequestException exception) {
			WebRequest.getErrorHandler().onError(exception);
		}

		return null;
	}

	/**
	 * Abstracts away tedious error handling by using a centralized
	 * {@link ErrorHandler error handler}.
	 */
	@Override
	public Request sendRequest(String data, RequestCallback callback) {
		try {
			return super.sendRequest(data, callback);
		}
		catch (RequestException exception) {
			WebRequest.getErrorHandler().onError(exception);
		}

		return null;
	}
}
