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
package mojo.gwt.ui.client.form;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ListBox;

import mojo.gwt.data.client.DataSource;
import mojo.gwt.data.client.event.LoadEvent;
import mojo.gwt.data.client.event.LoadHandler;
import mojo.gwt.data.client.type.ClassType;
import mojo.gwt.data.client.type.ClassTypeRegistry;

/**
 * ListBox that supports lazy loading.
 */
public class SelectBox extends ListBox {

	private String valueField;
	private String descrField;
	private DataSource<?> source;
	private boolean loaded;

	public SelectBox() {
		addStyleName("SelectBox");
		addClickHandler(new OnClick());
	}

	/**
	 * The record field that will be submitted.
	 */
	public String getValueField() {
		return valueField;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	/**
	 * The record field that will be presented.
	 */
	public String getDescrField() {
		return descrField;
	}

	public void setDescrField(String descrField) {
		this.descrField = descrField;
	}

	/**
	 * The data record source.
	 */
	public DataSource<?> getSource() {
		return source;
	}

	public void setStore(DataSource<?> source) {
		source.addLoadHandler(new OnLoad());
		this.source = source;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * Sets the currently selected entry by matching the submit value.
	 */
	public void setSelectedValue(String value) {
		for (int x = 0; x < getItemCount(); x++) {
			if (getValue(x).equals(value)) {
				setSelectedIndex(x);
				return;
			}
		}

		if (!isLoaded()) {
			addItem(value, value);
			setSelectedIndex(getItemCount() - 1);
		}
	}

	/**
	 * Initiates the data loading process.
	 */
	protected class OnClick implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (!isLoaded()) {
				addItem("Loading...");
				getSource().loadData();
			}
		}
	}

	/**
	 * Finalizes the data loading process.<br />
	 * Populates the list of items with the loaded list of records.
	 */
	protected class OnLoad implements LoadHandler {

		@Override
		public void onLoad(LoadEvent<?> event) {
			setLoaded(true);

			// keep current selection
			int idx = getSelectedIndex();
			String val = idx >= 0 ? getValue(idx) : null;

			clear();
			addItem("");

			for (Object record : event.getData()) {
				String value = extract(record, valueField);
				String descr = extract(record, descrField);
				addItem(descr, value);
			}

			if (val != null) {
				// restore selection
				setSelectedValue(val);
			}
		}

		/**
		 * Extracts field value from data record.<br />
		 * Ensures that the result will be a not-null string.
		 */
		@SuppressWarnings("unchecked")
		protected String extract(Object record, String field) {
			ClassType<Object> classType = (ClassType<Object>) ClassTypeRegistry.get(record.getClass());
			Object value = classType.getProperty(record, field);
			return value != null ? value.toString() : "";
		}
	}
}
