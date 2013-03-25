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

import java.util.List;

import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

public class RequiredValidation extends FieldValidation {

	public RequiredValidation(FieldPanel<?> panel) {
		super(panel);
	}

	@Override
	public void onSubmit(SubmitEvent event) {
		List<?> values = getValues();

		if (!assertRequired(values)) {
			getPanel().setError(getError());
			event.cancel();
		}
		else {
			getPanel().setError(null);
		}
	}

	protected boolean assertRequired(List<?> values) {
		for (Object value : values) {
			if (value != null) {
				if (value instanceof String) {
					String str = (String) value;

					if (!str.isEmpty()) {
						return true;
					}
				}
				else if (value instanceof Boolean) {
					Boolean bool = (Boolean) value;

					if (bool.booleanValue()) {
						return true;
					}
				}
				else {
					return true;
				}
			}
		}

		return false;
	}
}
