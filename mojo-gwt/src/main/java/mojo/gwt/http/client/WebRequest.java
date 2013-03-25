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

import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.URL;

public class WebRequest {

	/** Global error handler */
	private static ErrorHandler errorHandler;

	public static ErrorHandler getErrorHandler() {
		if (errorHandler == null) {
			errorHandler = new ErrorHandler();
		}

		return errorHandler;
	}

	public static void setErrorHandler(ErrorHandler handler) {
		errorHandler = handler;
	}

	/**
	 * Convenience method.
	 */
	public static void GET(String url, RequestCallback callback) {
		WebRequestBuilder builder = new WebRequestBuilder(RequestBuilder.GET, url);
		builder.setCallback(callback);
		builder.send();
	}

	/**
	 * Convenience method.
	 */
	public static void POST(String url, RequestCallback callback) {
		WebRequestBuilder builder = new WebRequestBuilder(RequestBuilder.POST, url);
		builder.setCallback(callback);
		builder.send();
	}

	/**
	 * Converts a map of data into a URL compatible string.
	 */
	public static String getFormData(Map<String, Object> data) {
		if (data != null && !data.isEmpty()) {
			StringBuilder sb = new StringBuilder();

			for (Entry<String, Object> entry : data.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (key != null && !key.isEmpty() && value != null) {
					sb.append(URL.encodeQueryString(key));
					sb.append('=');
					sb.append(URL.encodeQueryString(value.toString()));

					sb.append('&');
				}
			}

			if (sb.length() > 0) {
				return sb.substring(0, sb.length() - 1);
			}
		}

		return "";
	}
}
