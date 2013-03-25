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
package mojo.gwt.data.client.util;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

import mojo.gwt.data.client.convert.base.DateConverter;

public class JSObject extends JavaScriptObject {

	protected JSObject() {
	}

	public final Date getDate(String property) {
		DateConverter converter = new DateConverter();
		return converter.parse(getString(property));
	}

	public final void setDate(String property, Date value) {
		DateConverter converter = new DateConverter();
		setString(property, converter.format(value));
	}

	public native final int getInt(String property)
	/*-{
		return this[property] || 0;
	}-*/;

	public native final void setInt(String property, int value)
	/*-{
		this[property] = value;
	}-*/;

	public native final double getDouble(String property)
	/*-{
		return this[property] || 0.0;
	}-*/;

	public native final void setDouble(String property, double value)
	/*-{
		this[property] = value;
	}-*/;

	public native final boolean getBoolean(String property)
	/*-{
		return this[property] || false;
	}-*/;

	public native final void setBoolean(String property, boolean value)
	/*-{
		this[property] = value;
	}-*/;

	public native final String getString(String property)
	/*-{
		return this[property] || null;
	}-*/;

	public native final void setString(String property, String value)
	/*-{
		this[property] = value;
	}-*/;

	public native final <O extends JavaScriptObject> O getObject(String property)
	/*-{
		return this[property] || null;
	}-*/;

	public native final <O extends JavaScriptObject> void setObject(String property, O value)
	/*-{
		this[property] = value;
	}-*/;
}
