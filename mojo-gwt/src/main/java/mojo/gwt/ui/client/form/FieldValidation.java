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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HasValue;

public abstract class FieldValidation implements SubmitHandler {

	private FieldPanel<?> panel;
	private String error;

	public FieldValidation(FieldPanel<?> panel) {
		setPanel(panel);
		error = "Error";
	}

	public FieldPanel<?> getPanel() {
		return panel;
	}

	public void setPanel(FieldPanel<?> panel) {
		this.panel = panel;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	protected List<?> getValues() {
		int capacity = getPanel().getFields().length;
		List<Object> values = new ArrayList<Object>(capacity);

		for (FocusWidget field : getPanel().getFields()) {
			if (field instanceof HasValue<?>) {
				Object value = ((HasValue<?>) field).getValue();
				values.add(value);
			}
		}

		return values;
	}
}
