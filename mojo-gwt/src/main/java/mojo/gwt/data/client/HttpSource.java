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
package mojo.gwt.data.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.Response;

import mojo.gwt.http.client.WebRequest;
import mojo.gwt.http.client.WebRequestBuilder;
import mojo.gwt.http.client.WebRequestCallback;

public class HttpSource<T> extends DataSource<T> {

	private String url;
	private Method method;
	private Map<String, Object> params;

	public HttpSource(String url) {
		setUrl(url);
		setMethod(RequestBuilder.GET);
	}

	public String getUrl() {
		return url;
	}

	protected void setUrl(String url) {
		assert url != null;
		this.url = url;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		assert method != null;
		this.method = method;
	}

	public Map<String, Object> getParams() {
		if (params == null) {
			params = new HashMap<String, Object>();
		}

		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	@Override
	public void loadData() {
		WebRequestBuilder builder = null;
		String data = WebRequest.getFormData(getParams());

		if (RequestBuilder.GET.equals(method)) {
			// pass the parameters as part of the URL
			StringBuilder sb = new StringBuilder(url);

			if (!data.isEmpty()) {
				sb.append("?");
				sb.append(data);
			}

			builder = new WebRequestBuilder(method, sb.toString());
		}
		else {
			// pass the parameters as part of the request body
			builder = new WebRequestBuilder(method, url);
			builder.setFormData(data);
		}

		builder.setCallback(new LoadRequestCallback());
		builder.send();
	}

	private class LoadRequestCallback extends WebRequestCallback {

		@Override
		public void onResponseReceived(Request request, Response response) {
			if (response.getStatusCode() == Response.SC_OK) {
				readData(response.getText());
				getParams().clear();
			}
		}
	}
}
