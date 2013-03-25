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

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

import mojo.gwt.data.client.event.LoadEvent;
import mojo.gwt.data.client.event.LoadHandler;
import mojo.gwt.data.client.util.Observable;

/**
 * Data retrieval gateway.
 */
public abstract class DataSource<T> extends Observable {

	private DataReader<T> reader;

	public DataReader<T> getReader() {
		return reader;
	}

	public void setReader(DataReader<T> reader) {
		this.reader = reader;
	}

	public HandlerRegistration addLoadHandler(LoadHandler handler) {
		return addHandler(LoadEvent.TYPE, handler);
	}

	/**
	 * Fires the data conversion process. Requires a valid reader.
	 */
	public void readData(Object rawData) {
		List<T> data = getReader().convert(rawData);
		fireEvent(new LoadEvent<T>(data));
	}

	/**
	 * Fires the data loading process.
	 */
	public abstract void loadData();
}
