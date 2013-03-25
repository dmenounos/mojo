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
import com.google.gwt.http.client.RequestCallback;

/**
 * Abstracts away tedious error handling by using a centralized
 * {@link ErrorHandler error handler}.
 */
public abstract class WebRequestCallback implements RequestCallback {

	@Override
	public void onError(Request request, Throwable exception) {
		WebRequest.getErrorHandler().onError(exception);
	}
}
