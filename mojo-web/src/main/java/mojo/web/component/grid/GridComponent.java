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
package mojo.web.component.grid;

import java.util.ArrayList;
import java.util.List;

import mojo.web.component.UIComponent;
import mojo.web.util.BeanUtils;

public class GridComponent<T> extends UIComponent {

	private List<Column> columns;
	private List<Record> records;

	private String searchField;
	private String searchValue;

	private String title;

	public GridComponent() {
		columns = new ArrayList<Column>();
	}

	public void initRecords(List<T> objects) {
		if (objects == null) {
			return;
		}

		records = new ArrayList<Record>();

		for (T object : objects) {
			String[] values = new String[columns.size()];
			int cursor = 0;

			for (Column column : columns) {
				String value = column.render(object);
				values[cursor++] = value;
			}

			Record record = addRecord();
			record.setValues(values);
		}
	}

	public Column addColumn() {
		Column column = new Column();
		getColumns().add(column);
		return column;
	}

	public Record addRecord() {
		Record record = new Record();
		getRecords().add(record);
		return record;
	}

	//	@Override
	//	public void onSubmit(BindingResult result) {
	//		super.onSubmit(result);
	//
	//		if (searchField == null || searchField.isEmpty()) {
	//			result.addError(new FieldError("qwe", "personGrid.searchField", "LOLO 1"));
	//		}
	//	}

	public List<Column> getColumns() {
		return columns;
	}

	public List<Record> getRecords() {
		return records;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		logger.debug("--> setSearchField({})", searchField);
		this.searchField = searchField;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		logger.debug("--> setSearchValue({})", searchValue);
		this.searchValue = searchValue;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public interface Renderer {

		String render(Object obj);
	}

	public static class Column implements Renderer {

		private String name;
		private String label;
		private String width;
		private Renderer renderer;

		public Column() {
		}

		public Column(String name, String label) {
			setName(name);
			setLabel(label);
		}

		@Override
		public String render(Object obj) {
			if (renderer != null) {
				return renderer.render(obj);
			}

			return BeanUtils.getProperty(obj, name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getWidth() {
			return width;
		}

		public void setWidth(String width) {
			this.width = width;
		}

		public Renderer getRenderer() {
			return renderer;
		}

		public void setRenderer(Renderer renderer) {
			this.renderer = renderer;
		}
	}

	public static class Record {

		private String[] values;

		public String[] getValues() {
			return values;
		}

		public void setValues(String[] values) {
			this.values = values;
		}
	}
}
